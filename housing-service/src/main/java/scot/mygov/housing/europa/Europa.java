package scot.mygov.housing.europa;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import scot.mygov.housing.MetricName;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;

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
            EuropaResults results = target
                    .queryParam(paramName, paramValue)
                    .queryParam("fieldset", "all")
                    .queryParam("addresstype", "dpa")
                    .request()
                    .get(EuropaResults.class);
            timer.stop();
            return results;
        } catch (ProcessingException | WebApplicationException ex) {
            errorCounter.inc();
            errorMeter.mark();
            throw new EuropaException("Failed to lookup postcode", ex);
        }
    }
}
