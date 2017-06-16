package scot.mygov.housing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.rpz.PostcodeSource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/health")
public class Healthcheck {

    private static final Logger LOG = LoggerFactory.getLogger(Healthcheck.class);

    public static final String KNOWN_POSTCODE = "EH66QQ";
    private final PostcodeSource postcodeSource;

    @Inject
    public Healthcheck(PostcodeSource postcodeSource) {
        this.postcodeSource = postcodeSource;
    }

    @GET
    @Produces("application/json")
    public Response health() {
        try {
            Postcode postcode = postcodeSource.postcode(KNOWN_POSTCODE);

            if (!postcode.getPostcode().equals(KNOWN_POSTCODE)) {
                return Response.status(500)
                        .entity(new HealthResult(false, "Unexpected postcode:"+postcode.getPostcode()))
                        .build();
            }
            return Response.status(200).entity(new HealthResult(true, "ok")).build();

        } catch (Throwable t) {
            LOG.warn("Failed to fetch postcode in healthcheck", t);
            return Response.status(500).entity(new HealthResult(false, t.getMessage())).build();
        }
    }

    public static class HealthResult {
        private final boolean ok;
        private final String message;

        public HealthResult(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }

        public boolean isOk() {
            return ok;
        }

        public String getMessage() {
            return message;
        }
    }

}
