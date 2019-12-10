package scot.mygov.housing;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metered;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.cpi.CPIService;
import scot.mygov.housing.cpi.CPIServiceException;
import scot.mygov.housing.cpi.model.CPIData;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.fairrentregister.FairRentResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;


@Path("health")
@Produces(MediaType.APPLICATION_JSON)
public class Healthcheck {

    private static final Logger LOG = LoggerFactory.getLogger(Healthcheck.class);

    private static final double EPSILON = 0.00001;

    @Inject
    Healthcheck() {
        //Default constructor
    }

    @Inject
    HousingConfiguration housingConfiguration;

    @Inject
    AsposeLicense asposeLicense;

    @Inject
    CPIService cpiService;

    @Inject
    Europa europa;

    @Inject
    MetricRegistry metricRegistry;

    @Named(HousingModule.ES_RPZ_HEALTH_TARGET)
    @Inject
    WebTarget esRPZHealthTarget;

    @Inject
    DocumentGenerationService<ModelTenancy> modelTenancyService;

    @Inject
    FairRentResource fairRentResource;

    @GET
    public Response health(
            @QueryParam("licenseDays") @DefaultValue("10") int licenseDays
    ) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode result = factory.objectNode();

        ArrayNode errors = factory.arrayNode();
        ArrayNode warnings = factory.arrayNode();
        ObjectNode data = factory.objectNode();

        addLicenseInfo(result, errors, warnings, data, licenseDays);
        addCPIInfo(result, errors, data);
        addPostcodeLookupMetricsInfo(result, errors, data);
        addModelTenancyMetricsInfo(result, errors, data);
        addRPZElasticsearchInfo(result, errors, data);
        addDocumentGenerationMetricsInfo(result, errors, data, modelTenancyService);
        addFairRentMetricsInfo(result, warnings, data);

        boolean ok = errors.size() == 0;
        result.put("ok", ok);
        if (!ok) {
            result.set("errors", errors);
        }
        if (warnings.size() > 0) {
            result.set("warnings", warnings);
        }
        if (data.size() > 0) {
            result.set("data", data);
        }

        if (warnings.size() == 0 && errors.size() == 0) {
            Long daysUntilExpiry = asposeLicense.daysUntilExpiry();
            result.put("message", format("Aspose Words license expires in %d days", daysUntilExpiry));
        }

        int status = ok ? 200 : 503;
        return Response.status(status)
                .entity(result)
                .build();
    }

    private boolean addLicenseInfo(
            ObjectNode result,
            ArrayNode errors,
            ArrayNode warnings,
            ObjectNode data,
            int licenseDays) {
        boolean licensed = asposeLicense.isLicensed();
        LocalDate expires = asposeLicense.expires();
        Long daysRemaining = asposeLicense.daysUntilExpiry();
        result.put("license", licensed);
        if (expires != null) {
            data.put("licenseExpires", expires.toString());
            data.put("daysUntilExpiry", daysRemaining);
        }
        if (!licensed) {
            errors.add("Aspose Words license is not valid");
        } else if (daysRemaining < licenseDays) {
            warnings.add(format("License expires in %d days", daysRemaining));
        }
        return licensed;
    }

    private void addCPIInfo(ObjectNode result, ArrayNode errors, ObjectNode data) {
        boolean ok = true;

        // fetch the cpi data.
        try {
            CPIData cpiData = cpiService.cpiData();
            LocalDate releaseDate = cpiData.getReleaseDate();
            LocalDateTime nextreleaseDate = cpiData.getNextRelease().atStartOfDay();
            data.put("cpiReleaseDate", releaseDate.toString());
            data.put("cpiNextReleaseDate", nextreleaseDate.toString());

            // have we passed the date and time we expect the new data to have been released by?
            Duration gracePeriod = Duration.parse(housingConfiguration.getCpiGracePeriod());
            LocalDateTime latestAcceptableReleaseTime = nextreleaseDate.plus(gracePeriod);

            if (LocalDateTime.now().compareTo(latestAcceptableReleaseTime) > 0) {
                // we have passed the data that the new data should have been released
                errors.add("Next cpi release date has passed");
                ok = false;
            }
        } catch (CPIServiceException e) {
            LOG.error("Failed to get CPI data", e);
            errors.add("CPI data is not available");
            ok = false;
        }

        result.put("cpi", ok);
    }

    private void addPostcodeLookupMetricsInfo(ObjectNode result, ArrayNode errors, ObjectNode data) {

        Meter errorRate = metricRegistry.getMeters().get(MetricName.ERROR_RATE.name(europa));
        Timer timer = metricRegistry.getTimers().get(MetricName.RESPONSE_TIMES.name(europa));
        double responseTimeThreshold = housingConfiguration.getPostcodeLookupResponseTimeThreshold();
        boolean ok = errorRate.getFiveMinuteRate() == 0 && timer.getFiveMinuteRate() < responseTimeThreshold;

        if (errorRate.getFiveMinuteRate() > EPSILON) {
            errors.add("Postcode lookup errors in the last 5 minutes");
        }

        if (timer.getFiveMinuteRate() > responseTimeThreshold) {
            errors.add("Slow Postcode lookups in the last 5 minutes");
        }

        // collect all of the metrics for europa and add them to the data
        MetricFilter filter = forClass(europa.getClass());
        for (Map.Entry<String, Timer> entry : metricRegistry.getTimers(filter).entrySet()) {
            data.put(entry.getKey(), formatSnapshot(entry.getValue().getSnapshot()));
        }

        for (Map.Entry<String, Meter> entry : metricRegistry.getMeters(filter).entrySet()) {
            data.put(entry.getKey(), formatMeter(entry.getValue()));
        }

        for (Map.Entry<String, Counter> entry : metricRegistry.getCounters(filter).entrySet()) {
            data.put(entry.getKey(), entry.getValue().getCount());
        }

        result.put("Postcode lookups", ok);
    }

    private void addModelTenancyMetricsInfo(ObjectNode result, ArrayNode errors, ObjectNode data) {

        Meter errorRate = metricRegistry.getMeters().get(MetricName.ERROR_RATE.name(modelTenancyService));
        Timer timer = metricRegistry.getTimers().get(MetricName.RESPONSE_TIMES.name(modelTenancyService));

        data.put("modelTenancyErrorRate", formatMeter(errorRate));
        data.put("modelTenancyTimer", formatMeter(timer));

        // collect all of the metrics for modelTenancyService and add them to the data
        MetricFilter filter = forClass(modelTenancyService.getClass());
        for (Map.Entry<String, Timer> entry : metricRegistry.getTimers(filter).entrySet()) {
            data.put(entry.getKey(), formatSnapshot(entry.getValue().getSnapshot()));
        }

        for (Map.Entry<String, Meter> entry : metricRegistry.getMeters(filter).entrySet()) {
            data.put(entry.getKey(), formatMeter(entry.getValue()));
        }

        for (Map.Entry<String, Counter> entry : metricRegistry.getCounters(filter).entrySet()) {
            data.put(entry.getKey(), entry.getValue().getCount());
        }
    }

    private <T> void addDocumentGenerationMetricsInfo(ObjectNode result, ArrayNode errors, ObjectNode data, DocumentGenerationService<T> service) {

        String errorRateKey = MetricName.ERROR_RATE.name(service);
        Meter errorRate = metricRegistry.getMeters().get(errorRateKey);
        data.put(errorRateKey, formatMeter(errorRate));

        String timerKey = MetricName.RESPONSE_TIMES.name(service);
        Timer timer = metricRegistry.getTimers().get(timerKey);
        data.put(timerKey, formatMeter(timer));

        // collect all of the metrics for modelTenancyService and add them to the data
        MetricFilter filter = forClass(service.getClass());
        for (Map.Entry<String, Timer> entry : metricRegistry.getTimers(filter).entrySet()) {
            data.put(entry.getKey(), formatSnapshot(entry.getValue().getSnapshot()));
        }

        for (Map.Entry<String, Meter> entry : metricRegistry.getMeters(filter).entrySet()) {
            data.put(entry.getKey(), formatMeter(entry.getValue()));
        }

        for (Map.Entry<String, Counter> entry : metricRegistry.getCounters(filter).entrySet()) {
            data.put(entry.getKey(), entry.getValue().getCount());
        }
    }

    private void addFairRentMetricsInfo(ObjectNode result, ArrayNode warnings, ObjectNode data) {

        Meter errorRate = metricRegistry.getMeters().get(MetricName.ERROR_RATE.name(fairRentResource));
        Timer timer = metricRegistry.getTimers().get(MetricName.RESPONSE_TIMES.name(fairRentResource));

        if (errorRate.getFiveMinuteRate() > EPSILON) {
            warnings.add("Fair rent exceptions in the last 5 minutes");
        }

        data.put("fairRentErrorRate", formatMeter(errorRate));
        data.put("fairRentTimer", formatMeter(timer));

        // collect all of the metrics for fairRentRegister and add them to the data
        MetricFilter filter = forClass(fairRentResource.getClass());
        for (Map.Entry<String, Timer> entry : metricRegistry.getTimers(filter).entrySet()) {
            data.put(entry.getKey(), formatSnapshot(entry.getValue().getSnapshot()));
        }

        for (Map.Entry<String, Meter> entry : metricRegistry.getMeters(filter).entrySet()) {
            data.put(entry.getKey(), formatMeter(entry.getValue()));
        }

        for (Map.Entry<String, Counter> entry : metricRegistry.getCounters(filter).entrySet()) {
            data.put(entry.getKey(), entry.getValue().getCount());
        }
    }

    private String formatSnapshot(Snapshot ss) {
        return String.format(
                "min: %d, " +
                "max: %d, " +
                "mean: %.02f, " +
                "median: %.02f, " +
                "75th percentile: %.02f, " +
                "95th percentile: %.02f, " +
                "99th percentile: %.02f",
                toMillis(ss.getMin()),
                toMillis(ss.getMax()),
                toMillis(ss.getMean()),
                toMillis(ss.getMedian()),
                toMillis(ss.get75thPercentile()),
                toMillis(ss.get95thPercentile()),
                toMillis(ss.get99thPercentile()));
    }

    private long toMillis(long nanos) {
        return TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS);
    }

    private double toMillis(double nanos) {
        return nanos / TimeUnit.MILLISECONDS.toNanos(1);
    }

    private String formatMeter(Metered m) {
        if (m == null) {
            return "null meter";
        }
        return String.format("count: %d, meanRate: %.02f, oneMinRate: %.02f, fiveMinRate: %.02f, fifteenMinRate: %.02f",
                m.getCount(), m.getMeanRate(),
                m.getOneMinuteRate(),  m.getFiveMinuteRate(), m.getFifteenMinuteRate() );
    }

    private void addRPZElasticsearchInfo(ObjectNode result, ArrayNode errors, ObjectNode data) {
        boolean ok = true;
        try {
            Response response = esRPZHealthTarget.request().get();

            if (response.getStatus() != 200) {
                ok = false;
                errors.add("Non 200 response code from Elasticsearch RPZ health target: " + response.getStatus());
            }
            response.close();
        } catch (ProcessingException | WebApplicationException e) {
            ok = false;
            errors.add("Exception trying to get RPZ data from ES:" + e.getMessage());
            LOG.error("Failed to get RPZ info from Elasticsearch", e);
        }
        result.put("RPZ Elasticsearch Data", ok);
     }

    private MetricFilter forClass(Class clazz) {
        return (name, metric) -> name.startsWith(clazz.getName());
    }
}
