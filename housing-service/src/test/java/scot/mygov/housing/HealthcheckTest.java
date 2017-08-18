package scot.mygov.housing;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
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
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.postcode.PostcodeService;
import scot.mygov.housing.postcode.PostcodeServiceException;
import scot.mygov.housing.postcode.PostcodeServiceResults;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.SortedMap;
import java.util.Map;
import java.util.TreeMap;

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
        healthcheck.metricRegistry = new MetricRegistry();
        healthcheck.mapcloud = new Mapcloud(mock(WebTarget.class), "", "", healthcheck.metricRegistry);
        healthcheck.housingConfiguration = new HousingConfiguration();
        healthcheck.asposeLicense = anyValidLicense();
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
    public void notOkWhenDataIsMoreThanAMonthOld() throws CPIServiceException, IOException {
        LocalDate nextRelease = LocalDate.now().plusDays(1);
        LocalDate release = LocalDate.now().minusMonths(1).minusDays(1);
        this.healthcheck.cpiService = cpiServiceWithDates(nextRelease, release);

        dispatcher.invoke(request, response);

        assertEquals(503, response.getStatus());
        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("cpi not as expected", false, health.get("cpi").asBoolean());
    }

    @Test
    public void notOkPostcodeServiceHasErrors() throws CPIServiceException, IOException, InterruptedException {

        this.healthcheck.metricRegistry = mockMetricsRegistry(10, 0);

        dispatcher.invoke(request, response);

        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("postcode not as expected", false, health.get("postcode").asBoolean());
        assertEquals(503, response.getStatus());
    }

    @Test
    public void notOkPostcodeServiceResponseTimeIsSlow() throws CPIServiceException, IOException, InterruptedException {

        this.healthcheck.metricRegistry = mockMetricsRegistry(0, 501);

        dispatcher.invoke(request, response);

        JsonNode health = mapper.readTree(response.getContentAsString());
        assertEquals("postcode not as expected", false, health.get("postcode").asBoolean());
        assertEquals(503, response.getStatus());
    }

    private MetricRegistry mockMetricsRegistry(double errorFiveMinRate, double responseTimesFiveMinuteRate) {

        MetricRegistry registry = mock(MetricRegistry.class);

        SortedMap<String, Meter> meters = new TreeMap<>();
        Meter errorRate = mock(Meter.class);
        when(errorRate.getFiveMinuteRate()).thenReturn(errorFiveMinRate);
        meters.put(MetricName.ERROR_RATE.name(healthcheck.mapcloud), errorRate);
        when(registry.getMeters()).thenReturn(meters);
        when(registry.getMeters(Mockito.any())).thenReturn(meters);

        SortedMap<String, Timer> timers = new TreeMap<>();
        Timer responseTimes = mock(Timer.class);
        when(responseTimes.getFiveMinuteRate()).thenReturn(responseTimesFiveMinuteRate);
        timers.put(MetricName.RESPONSE_TIMES.name(healthcheck.mapcloud), responseTimes);
        when(registry.getTimers()).thenReturn(timers);
        when(registry.getTimers(Mockito.any())).thenReturn(timers);

        SortedMap<String, Counter> counters = new TreeMap<>();
        Counter errorCounter = mock(Counter.class);
        when(errorCounter.getCount()).thenReturn(0L);
        counters.put(MetricName.ERRORS.name(healthcheck.mapcloud), errorCounter);
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

    private class DummyPostcodeService implements PostcodeService {
        private final Timer responseTimes;

        private final Counter requestCounter;

        private final Counter errorCounter;

        private final Meter requestMeter;

        private final Meter errorMeter;

        public DummyPostcodeService(MetricRegistry registry) {
            this.responseTimes = registry.timer(MetricName.RESPONSE_TIMES.name(this));
            this.requestCounter = registry.counter(MetricName.REQUESTS.name(this));
            this.errorCounter = registry.counter(MetricName.ERRORS.name(this));
            this.requestMeter = registry.meter(MetricName.REQUEST_RATE.name(this));
            this.errorMeter = registry.meter(MetricName.ERROR_RATE.name(this));
        }

        public PostcodeServiceResults lookup(String postcode) throws PostcodeServiceException {
            return null;
        }
    }
}
