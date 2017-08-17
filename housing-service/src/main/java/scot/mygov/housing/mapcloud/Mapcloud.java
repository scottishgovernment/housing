package scot.mygov.housing.mapcloud;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import scot.mygov.housing.MetricName;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import java.util.Base64;

/**
 * Client for Mapcloud web service
 */
public class Mapcloud {

    private final WebTarget mapcloudTarget;

    private final String user;

    private final String password;

    private final Timer responseTimes;

    private final Counter requestCounter;

    private final Counter errorCounter;

    private final Meter requestMeter;

    private final Meter errorMeter;

    public Mapcloud(
            WebTarget mapcloudTarget,
            String user,
            String password,
            MetricRegistry registry) {
        this.mapcloudTarget = mapcloudTarget;
        this.user = user;
        this.password = password;

        this.responseTimes = registry.timer(MetricName.RESPONSE_TIMES.name(this));
        this.requestCounter = registry.counter(MetricName.REQUESTS.name(this));
        this.errorCounter = registry.counter(MetricName.ERRORS.name(this));
        this.requestMeter = registry.meter(MetricName.REQUEST_RATE.name(this));
        this.errorMeter = registry.meter(MetricName.ERROR_RATE.name(this));
    }

    public MapcloudResults lookupPostcode(String postcode) throws MapcloudException {
        return lookup("address/addressbase/postcode", "pc", postcode);
    }

    public MapcloudResults lookupUprn(String uprn) throws MapcloudException {
        return lookup("address/addressbase/uprn", "uprn", uprn);
    }

    private MapcloudResults lookup(String url, String paramName, String paramValue) throws MapcloudException {
        Timer.Context timer = responseTimes.time();
        requestCounter.inc();
        requestMeter.mark();
        try {
            MapcloudResults results = mapcloudTarget
                    .path(url)
                    .queryParam(paramName, paramValue)
                    .queryParam("addrformat", 2)
                    .request()
                    .header("Authorization", authHeader())
                    .get(MapcloudResults.class);
            timer.stop();
            return results;
        } catch (ProcessingException | WebApplicationException ex) {
            errorCounter.inc();
            errorMeter.mark();
            throw new MapcloudException("Failed to lookup postcode", ex);
        }
    }

    private String authHeader() {
        String userAndPassword = user + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
        return "Basic " + encoded;
    }
}