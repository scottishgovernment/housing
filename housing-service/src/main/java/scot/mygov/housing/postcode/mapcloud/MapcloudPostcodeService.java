package scot.mygov.housing.postcode.mapcloud;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.MetricName;
import scot.mygov.housing.postcode.PostcodeService;
import scot.mygov.housing.postcode.PostcodeServiceException;
import scot.mygov.housing.postcode.PostcodeServiceResult;
import scot.mygov.housing.postcode.PostcodeServiceResults;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MapcloudPostcodeService implements PostcodeService {

    private final WebTarget mapcloudTarget;

    private final String user;

    private final String password;

    private final Timer responseTimes;

    private final Counter requestCounter;

    private final Counter errorCounter;

    private final Meter requestMeter;

    private final Meter errorMeter;

    public MapcloudPostcodeService(
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

    public PostcodeServiceResults lookup(String postcode) throws PostcodeServiceException {
        return toResults(performLookup(postcode));
    }

    private MapcloudPostcodeResults performLookup(String postcode) throws PostcodeServiceException {
        Timer.Context timer = responseTimes.time();
        requestCounter.inc();
        requestMeter.mark();
        try {
            MapcloudPostcodeResults results = mapcloudTarget
                    .path("address/addressbase/postcode")
                    .queryParam("pc", postcode)
                    .queryParam("addrformat", 2)
                    .request()
                    .header("Authorization", authHeader())
                    .get(MapcloudPostcodeResults.class);
            timer.stop();
            return results;
        } catch (ProcessingException | WebApplicationException ex) {
            errorCounter.inc();
            errorMeter.mark();
            throw new PostcodeServiceException("", ex);
        }
    }

    private String authHeader() {
        String userAndPassword = user + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
        return "Basic " + encoded;
    }

    private PostcodeServiceResults toResults(MapcloudPostcodeResults from) {
        List<PostcodeServiceResult> to = from.getResults()
                .stream()
                .map(this::toResult)
                .collect(toList());
        PostcodeServiceResults res = new PostcodeServiceResults();
        res.setResults(to);
        return res;
    }

    private PostcodeServiceResult toResult(MapcloudPostcodeResult from) {
        PostcodeServiceResult to = new PostcodeServiceResult();
        to.setUprn(from.getUprn());
        List<String> addessLines = new ArrayList<>();
        addIfNotEmpty(from.getAddressLine1(), addessLines);
        addIfNotEmpty(from.getAddressLine2(), addessLines);
        addIfNotEmpty(from.getAddressLine3(), addessLines);
        to.setAddressLines(addessLines);
        to.setTown(from.getTown());
        to.setPostcode(from.getPostcode());
        return to;
    }

    private void addIfNotEmpty(String value, List<String> list) {
        if (!StringUtils.isEmpty(value)) {
            list.add(value);
        }
    }
}
