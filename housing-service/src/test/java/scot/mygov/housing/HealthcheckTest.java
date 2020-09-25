package scot.mygov.housing;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.documents.DocumentTemplateLoaderBasicImpl;
import scot.mygov.housing.cpi.CPIService;
import scot.mygov.housing.cpi.CPIServiceException;
import scot.mygov.housing.cpi.model.CPIData;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.fairrentregister.FairRentResource;
import scot.mygov.housing.forms.DocumentGenerationService;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HealthcheckTest {

    private ObjectMapper mapper;

    private Dispatcher dispatcher;

    private Healthcheck healthcheck;

    private MockHttpRequest request;

    private MockHttpResponse response;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        healthcheck = new Healthcheck();
        healthcheck.metricRegistry = new MetricRegistry();
        healthcheck.europa = new Europa(mock(WebTarget.class), healthcheck.metricRegistry);
        healthcheck.housingConfiguration = new HousingConfiguration();
        healthcheck.asposeLicense = anyValidLicense();
        healthcheck.cpiService = validCPIService();
        healthcheck.esRPZHealthTarget = validTarget();
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getRegistry().addSingletonResource(healthcheck);
        request = MockHttpRequest.get("health");
        request.accept(MediaType.APPLICATION_JSON_TYPE);
        response = new MockHttpResponse();
        DocumentTemplateLoader templateLoader = new DocumentTemplateLoaderBasicImpl("", healthcheck.asposeLicense);
        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
        healthcheck.modelTenancyService = Mockito.mock(DocumentGenerationService.class);
        healthcheck.fairRentResource = new FairRentResource(healthcheck.metricRegistry);
    }

    @Test
    public void okForGreeenpath() throws IOException {
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertTrue("licence not as expected", health.get("license").asBoolean());
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
    public void notOkPostcodeServiceHasErrors() throws CPIServiceException, IOException, InterruptedException {

        this.healthcheck.metricRegistry = mockMetricsRegistryEuropa(10, 0);

        dispatcher.invoke(request, response);

        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("postcode not as expected", false, health.get("Postcode lookups").asBoolean());
        assertEquals(503, response.getStatus());
    }

    @Test
    public void notOkPostcodeServiceResponseTimeIsSlow() throws CPIServiceException, IOException, InterruptedException {

        this.healthcheck.metricRegistry = mockMetricsRegistryEuropa(0, 501);

        dispatcher.invoke(request, response);

        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("postcode not as expected", false, health.get("Postcode lookups").asBoolean());
        assertEquals(503, response.getStatus());
    }

    @Test
    public void notOKIfErrorFromEZRPZ() throws CPIServiceException, IOException, InterruptedException {

        this.healthcheck.esRPZHealthTarget = errorTarget();

        dispatcher.invoke(request, response);

        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("error from postcode not as expected", false, health.get("RPZ Elasticsearch Data").asBoolean());
        assertEquals(503, response.getStatus());
    }

    @Test
    public void warningAddedIfFairRentResponseRateNotZero() throws CPIServiceException, IOException, InterruptedException {
        this.healthcheck.metricRegistry = mockMetricsRegistryFairRent(10, 0);
        dispatcher.invoke(request, response);
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertTrue(health.get("warnings").size() > 0);
    }

    @Test
    public void noWarningAddedIfFairRentResponseRateNotZero() throws CPIServiceException, IOException, InterruptedException {
        this.healthcheck.metricRegistry = mockMetricsRegistryFairRent(0, 0);
        dispatcher.invoke(request, response);
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertNull(health.get("warnings"));
    }
    private MetricRegistry mockMetricsRegistryEuropa(double errorFiveMinRate, double responseTimesFiveMinuteRate) {
        return mockMetricsRegistry(errorFiveMinRate, responseTimesFiveMinuteRate, 0, 0);
    }

    private MetricRegistry mockMetricsRegistryFairRent(double errorFiveMinRate, double responseTimesFiveMinuteRate) {
       return mockMetricsRegistry(0, 0, errorFiveMinRate, responseTimesFiveMinuteRate);
    }

    private MetricRegistry mockMetricsRegistry(double errorFiveMinRateEuropa,
                                               double responseTimesFiveMinuteRateEuropa,
                                               double errorFiveMinRateFairRent,
                                               double responseTimesFiveMinuteRateFairRent) {

        MetricRegistry registry = mock(MetricRegistry.class);

        SortedMap<String, Meter> meters = new TreeMap<>();
        Meter errorRateEuropa = mock(Meter.class);
        when(errorRateEuropa.getFiveMinuteRate()).thenReturn(errorFiveMinRateEuropa);
        meters.put(MetricName.ERROR_RATE.name(healthcheck.europa), errorRateEuropa);
        Meter errorRateFairRent = mock(Meter.class);
        when(errorRateFairRent.getFiveMinuteRate()).thenReturn(errorFiveMinRateFairRent);
        meters.put(MetricName.ERROR_RATE.name(healthcheck.fairRentResource), errorRateFairRent);
        when(registry.getMeters()).thenReturn(meters);
        when(registry.getMeters(Mockito.any())).thenReturn(meters);

        SortedMap<String, Timer> timers = new TreeMap<>();
        Snapshot snapshot = mock(Snapshot.class);
        Timer responseTimesEuropa = mock(Timer.class);
        when(responseTimesEuropa.getSnapshot()).thenReturn(snapshot);
        when(responseTimesEuropa.getFiveMinuteRate()).thenReturn(responseTimesFiveMinuteRateEuropa);
        timers.put(MetricName.RESPONSE_TIMES.name(healthcheck.europa), responseTimesEuropa);

        Timer responseTimesFairRent = mock(Timer.class);
        when(responseTimesFairRent.getSnapshot()).thenReturn(snapshot);
        when(responseTimesFairRent.getFiveMinuteRate()).thenReturn(responseTimesFiveMinuteRateFairRent);
        timers.put(MetricName.RESPONSE_TIMES.name(healthcheck.fairRentResource), responseTimesFairRent);

        when(registry.getTimers()).thenReturn(timers);
        when(registry.getTimers(Mockito.any())).thenReturn(timers);

        SortedMap<String, Counter> counters = new TreeMap<>();
        Counter errorCounterEuropa = mock(Counter.class);
        when(errorCounterEuropa.getCount()).thenReturn(0L);
        counters.put(MetricName.ERRORS.name(healthcheck.europa), errorCounterEuropa);

        Counter errorCounterFairRent = mock(Counter.class);
        when(errorCounterFairRent.getCount()).thenReturn(0L);
        counters.put(MetricName.ERRORS.name(healthcheck.fairRentResource), errorCounterFairRent);

        when(registry.getCounters()).thenReturn(counters);
        when(registry.getCounters(Mockito.any())).thenReturn(counters);

        return registry;
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

    private CPIService cpiServiceWithData(CPIData cpiData) throws CPIServiceException {
        CPIService service = mock(CPIService.class);
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
        CPIService service = mock(CPIService.class);
        Mockito.when(service.cpiData()).thenThrow(new CPIServiceException("error", new RuntimeException("runtime")));
        return service;
    }

    private WebTarget validTarget() {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);
        when(target.request()).thenReturn(builder);
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(200);
        return target;
    }

    private WebTarget errorTarget() {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);
        when(target.request()).thenReturn(builder);
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(500);
        return target;
    }
}
