package scot.mygov.housing.fairrentregister;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.HousingModule;
import scot.mygov.housing.MetricName;

import javax.inject.Inject;
import javax.inject.Named;

@Path("fairrent")
public class FairRentResource {

    private static final Logger LOG = LoggerFactory.getLogger(FairRentResource.class);

    @Inject
    HousingConfiguration configuration;

    @Inject
    @Named(HousingModule.FAIR_RENT_TARGET)
    WebTarget fairRentTarget;

    @Inject
    MetricRegistry registry;

    private final Timer responseTimes;

    private final Counter requestCounter;

    private final Counter errorCounter;

    private final Meter requestMeter;

    private final Meter errorMeter;

    @Inject
    public FairRentResource(MetricRegistry registry) {
        this.responseTimes = registry.timer(MetricName.RESPONSE_TIMES.name(this));
        this.requestCounter = registry.counter(MetricName.REQUESTS.name(this));
        this.errorCounter = registry.counter(MetricName.ERRORS.name(this));
        this.requestMeter = registry.meter(MetricName.REQUEST_RATE.name(this));
        this.errorMeter = registry.meter(MetricName.ERROR_RATE.name(this));
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public void search(
            @Suspended AsyncResponse response,
            @QueryParam("query") String query,
            @DefaultValue("0") @QueryParam("from") int from,
            @DefaultValue("10") @QueryParam("size") int size) {

        // we convert the incoming param "from" to "index" and "size" to "numberOfRecords"
        WebTarget target = fairRentTarget
                .path("/API/cases/singleSearch")
                .queryParam("searchTerm", query)
                .queryParam("numberOfRecords", size)
                .queryParam("index", from);
        request(response, target);
    }

    @GET
    @Path("cases/{case}")
    @Produces(MediaType.APPLICATION_JSON)
    public void caseDetails(@Suspended final AsyncResponse response, @PathParam("case") String caseId) {
        WebTarget target = fairRentTarget.path("/API/cases/" + caseId);
        request(response, target);
    }

    private void request(final AsyncResponse response, WebTarget target) {
        Timer.Context timer = responseTimes.time();
        requestCounter.inc();
        requestMeter.mark();
        InvocationCallback<Response> callback = new InvocationCallback<Response>() {
            @Override
            public void completed(Response targetResponse) {
                response.resume(Response.status(200).entity(targetResponse.getEntity()).build());
                timer.stop();
            }

            @Override
            public void failed(Throwable throwable) {
                errorCounter.inc();
                errorMeter.mark();
                LOG.error("Failed to get fair rent data, path is {}", target.getUri().getPath(), throwable);
                response.resume(Response.status(500).entity("Failed to get fair rent data").build());
                timer.stop();
            }
        };
        target.request().async().get(callback);
    }
}
