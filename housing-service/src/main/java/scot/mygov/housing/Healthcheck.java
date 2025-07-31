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
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.cpi.CPIService;
import scot.mygov.housing.cpi.CPIServiceException;
import scot.mygov.housing.cpi.model.CPIData;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import javax.inject.Inject;
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

    @Inject
    DocumentGenerationService<ModelTenancy> modelTenancyService;

    @Inject
    ErrorHandler errorHandler;

    @GET
    public Response health(
            @QueryParam("licenseDays") @DefaultValue("10") int licenseDays
    ) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode result = factory.objectNode();

        ArrayNode errors = factory.arrayNode();
        ArrayNode warnings = factory.arrayNode();
        ArrayNode info = factory.arrayNode();
        ObjectNode data = factory.objectNode();

        addLicenseInfo(result, errors, warnings, info, data, licenseDays);
        addCPIInfo(result, errors, data);
        addPostcodeLookupMetricsInfo(result, errors, data);
        addModelTenancyMetricsInfo(result, errors, data);
        addDocumentGenerationMetricsInfo(result, errors, data, modelTenancyService);
        addFormSubmissionErrorsInfo(errors);

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

    private void addLicenseInfo(
            ObjectNode result,
            ArrayNode errors,
            ArrayNode info,
            ArrayNode warnings,
            ObjectNode data,
            int licenseDays) {
        if (!asposeLicense.hasLicense()) {
            errors.add("No licence loaded");
        } else {
            LocalDate expires = asposeLicense.expires();
            Long daysRemaining = asposeLicense.daysUntilExpiry();
            if (expires != null) {
                data.put("licenseExpires", expires.toString());
                data.put("daysUntilExpiry", daysRemaining);
            }
        }
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
            Duration gracePeriod = Duration.parse(housingConfiguration.getCpi().getGraceperiod());
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

    private void addFormSubmissionErrorsInfo(ArrayNode errors) {
        Meter errorRate = metricRegistry.getMeters().get(MetricName.ERROR_RATE.name(errorHandler));
        if (errorRate.getFiveMinuteRate() > EPSILON) {
            errors.add("Form submission errors in last 5 minutes");
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

    private MetricFilter forClass(Class clazz) {
        return (name, metric) -> name.startsWith(clazz.getName());
    }
}
