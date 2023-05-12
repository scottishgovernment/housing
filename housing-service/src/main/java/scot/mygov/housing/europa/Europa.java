package scot.mygov.housing.europa;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.WebTarget;
import scot.mygov.housing.MetricName;

import java.util.Collections;

public class Europa {

    private WebTarget target;

    private final Timer responseTimes;

    private final Counter requestCounter;

    private final Counter errorCounter;

    private final Meter requestMeter;

    private final Meter errorMeter;

    public Europa(WebTarget target, MetricRegistry registry) {
        this.target = target;

        this.responseTimes = registry.timer(MetricName.RESPONSE_TIMES.name(this));
        this.requestCounter = registry.counter(MetricName.REQUESTS.name(this));
        this.errorCounter = registry.counter(MetricName.ERRORS.name(this));
        this.requestMeter = registry.meter(MetricName.REQUEST_RATE.name(this));
        this.errorMeter = registry.meter(MetricName.ERROR_RATE.name(this));
    }

    public EuropaResults lookupPostcode(String postcode) throws EuropaException {
        return lookup("postcode", postcode);
    }

    public EuropaResults lookupUprn(String uprn) throws EuropaException {
        return lookup("uprn", uprn);
    }

    private EuropaResults lookup(String paramName, String paramValue) throws EuropaException {
        Timer.Context timer = responseTimes.time();
        requestCounter.inc();
        requestMeter.mark();
        try {

            JsonNode results = target
                    .queryParam(paramName, paramValue)
                    .queryParam("fieldset", "all")
                    .queryParam("addresstype", "dpa")
                    .request()
                    .get(JsonNode.class);

            ObjectMapper mapper = new ObjectMapper();
            EuropaMetadata metadata = mapper.treeToValue(results.get("metadata"), EuropaMetadata.class);
            if (metadata.getCount() == 0) {
                return emptyResults(metadata);
            }
            timer.stop();
            return mapper.treeToValue(results, EuropaResults.class);
        } catch (JsonProcessingException | ProcessingException | WebApplicationException ex) {
            errorCounter.inc();
            errorMeter.mark();
            throw new EuropaException("Failed to lookup postcode", ex);
        }
    }

    private EuropaResults emptyResults(EuropaMetadata metadata) {
        EuropaResults results = new EuropaResults();
        results.setResults(Collections.emptyList());
        results.setMetadata(metadata);
        return results;
    }
}
