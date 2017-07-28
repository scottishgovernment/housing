package scot.mygov.housing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.housing.cpi.CPIService;
import scot.mygov.housing.cpi.CPIServiceException;
import scot.mygov.housing.cpi.model.CPIData;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HealthcheckTest {

    private ObjectMapper mapper;

    private Dispatcher dispatcher;

    private Healthcheck healthcheck;

    private MockHttpRequest request;

    private MockHttpResponse response;

    @Before
    public void setUp() throws URISyntaxException, CPIServiceException {
        mapper = new ObjectMapper();
        healthcheck = new Healthcheck();
        healthcheck.housingConfiguration = new HousingConfiguration();
        healthcheck.asposeLicense = anyValidLicense();
        healthcheck.geoHealthTarget = target(200);
        healthcheck.cpiService = validCPIService();
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getRegistry().addSingletonResource(healthcheck);
        request = MockHttpRequest.get("health");
        request.accept(MediaType.APPLICATION_JSON_TYPE);
        response = new MockHttpResponse();
    }

    @Test
    public void okForGreeenpath() throws IOException {
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertTrue("licence not as expected", health.get("license").asBoolean());
        assertTrue("geosearch not as expected", health.get("geosearch").asBoolean());
        JsonNode data = health.get("data");
        assertEquals("days until expiry not as expected", 100L, data.get("daysUntilExpiry").asLong());
        String expectedExpiry = healthcheck.asposeLicense.expires().toString();
        assertEquals("licenseExpires not as expected", expectedExpiry, data.get("licenseExpires").asText());
    }

    @Test
    public void messagePropertyPresentIfServiceIsHealthy() throws IOException {
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertFalse("message property should be non-empty", health.get("message").asText().isEmpty());
    }

    @Test
    public void warningIfLicenseWillExpireSoon() throws Exception {
        LocalDate expires = LocalDate.now().plusDays(20);
        this.healthcheck.asposeLicense = validLicense(expires, 20L);
        request = MockHttpRequest.get("health?licenseDays=30");

        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        JsonNode warnings = health.get("warnings");
        assertEquals(1, warnings.size());
        assertTrue(warnings.get(0).asText().contains("License"));
        JsonNode data = health.get("data");
        assertEquals("days until expiry not as expected", 20L, data.get("daysUntilExpiry").asLong());
        assertEquals("licenseExpires not as expected", expires.toString(), data.get("licenseExpires").asText());
    }

    @Test
    public void notOkForNotLicensed() throws IOException {
        this.healthcheck.asposeLicense = invalidLicense();

        dispatcher.invoke(request, response);

        assertEquals(503, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertFalse("licence not as expected", health.get("license").asBoolean());
        JsonNode errors = health.get("errors");
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).asText().contains("license"));
    }

    @Test
    public void notOkForExceptionFromGeo() throws IOException {
        this.healthcheck.geoHealthTarget = exceptionTarget();

        dispatcher.invoke(request, response);

        assertEquals(503, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertTrue("licence not as expected", health.get("license").asBoolean());
        assertFalse("geosearch not as expected", health.get("geosearch").asBoolean());
    }

    @Test
    public void notOkForErrorResponseFromGeo() throws IOException {
        this.healthcheck.geoHealthTarget = target(503);

        dispatcher.invoke(request, response);

        assertEquals(503, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("licence not as expected", true, health.get("license").asBoolean());
        assertEquals("geosearch not as expected", false, health.get("geosearch").asBoolean());
    }

    @Test
    public void notOkWhenCPIServiceThrowsException() throws CPIServiceException, IOException {
        this.healthcheck.cpiService = exceptionThrowingCPIService();

        dispatcher.invoke(request, response);

        assertEquals(503, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("cpi not as expected", false, health.get("cpi").asBoolean());
    }

    @Test
    public void notOkWhenNextReleaseDateHasPassed() throws CPIServiceException, IOException {
        LocalDate nextRelease = LocalDate.now().minusDays(1);
        LocalDate release = LocalDate.now().minusDays(10);
        this.healthcheck.cpiService = cpiServiceWithDates(nextRelease, release);

        dispatcher.invoke(request, response);

        assertEquals(503, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("cpi not as expected", false, health.get("cpi").asBoolean());
    }


    @Test
    public void notOkWhenDataIsMoreThanAMonthOld() throws CPIServiceException, IOException {
        LocalDate nextRelease = LocalDate.now().plusDays(1);
        LocalDate release = LocalDate.now().minusMonths(1).minusDays(1);
        this.healthcheck.cpiService = cpiServiceWithDates(nextRelease, release);

        dispatcher.invoke(request, response);

        assertEquals(503, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("cpi not as expected", false, health.get("cpi").asBoolean());
    }

    private AsposeLicense anyValidLicense() {
        AsposeLicense license = mock(AsposeLicense.class);
        when(license.isLicensed()).thenReturn(true);
        when(license.daysUntilExpiry()).thenReturn(100L);
        when(license.expires()).thenReturn(LocalDate.now());
        return license;
    }

    private AsposeLicense validLicense(LocalDate expires, long daysUntilExpiry) {
        AsposeLicense license = mock(AsposeLicense.class);
        when(license.isLicensed()).thenReturn(true);
        when(license.daysUntilExpiry()).thenReturn(daysUntilExpiry);
        when(license.expires()).thenReturn(expires);
        return license;
    }

    private AsposeLicense invalidLicense() {
        AsposeLicense license = mock(AsposeLicense.class);
        when(license.isLicensed()).thenReturn(false);
        return license;
    }

    private WebTarget exceptionTarget() {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(builder.get()).thenThrow(new ProcessingException("processing exception"));
        when(target.request()).thenReturn(builder);
        return target;
    }


    private WebTarget target(int status) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(status);
        when(builder.get()).thenReturn(response);
        when(target.request()).thenReturn(builder);
        return target;
    }

    private CPIService cpiServiceWithData(CPIData cpiData) throws CPIServiceException {
        CPIService service = Mockito.mock(CPIService.class);
        Mockito.when(service.cpiData()).thenReturn(cpiData);
        return service;
    }

    private CPIService cpiServiceWithDates(LocalDate nextRelease, LocalDate releaseDate) throws CPIServiceException {
        CPIData cpiData = new CPIData();
        cpiData.setNextRelease(nextRelease);
        cpiData.setReleaseDate(releaseDate);
        return cpiServiceWithData(cpiData);
    }

    private CPIService validCPIService() throws CPIServiceException {
        return cpiServiceWithDates(LocalDate.now().plusDays(1), LocalDate.now().minusDays(1));
    }

    private CPIService exceptionThrowingCPIService() throws CPIServiceException {
        CPIService service = Mockito.mock(CPIService.class);
        Mockito.when(service.cpiData()).thenThrow(new CPIServiceException("error", new RuntimeException("runtime")));
        return service;
    }

}
