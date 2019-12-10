package scot.mygov.housing.fairrentregister;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.HousingModule;
import scot.mygov.housing.MetricName;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("fairrent")
public class FairRentResource {

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
            @DefaultValue("0") @QueryParam("page") int from,
            @DefaultValue("10") @QueryParam("size") int size) {

        // We currently only search by townOrCity since the api currently AND's terms together.
        // This is being changed to OR them together at which stage we will add calls to
        // add streetName and postcode.
        WebTarget target = fairRentTarget
                .path("/FairRent/API/cases/search")
                .queryParam("townOrCity", query)
                .queryParam("index", from)
                .queryParam("numberOfRecords", 10);
        request(response, target);
    }

    @GET
    @Path("cases/{case}")
    @Produces(MediaType.APPLICATION_JSON)
    public void caseDetails(@Suspended final AsyncResponse response, @PathParam("case") String caseId) {
        WebTarget target = fairRentTarget.path("/FairRent/API/cases/" + caseId);
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
                response.resume(Response.status(500).entity("Failed to get fair rent data").build());
                timer.stop();
            }
        };
        target.request().async().get(callback);
    }
}
