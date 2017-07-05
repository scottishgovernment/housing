package scot.mygov.housing;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("health")
@Produces(MediaType.APPLICATION_JSON)
public class Healthcheck {

    private static final Logger LOG = LoggerFactory.getLogger(Healthcheck.class);

    private AsposeLicense asposeLicense;

    private WebTarget geoHealthTarget;

    @Inject
    public Healthcheck(AsposeLicense asposeLicense, WebTarget geoHealthTarget) {
        this.asposeLicense = asposeLicense;
        this.geoHealthTarget = geoHealthTarget;
    }

    @GET
    public Response health() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode result = factory.objectNode();
        boolean licensed = asposeLicense.isLicensed();
        LocalDate expires = asposeLicense.expires();
        boolean geosearchOK = geosearchHealth() == 200;
        result.put("license", licensed);
        if (expires != null) {
            result.put("licenseExpires", expires.toString());
            result.put("daysUntilExpiry", asposeLicense.daysUntilExpiry());
        }
        result.put("geosearch", geosearchOK);
        boolean ok = licensed && geosearchOK;
        int status = ok ? 200 : 503;
        return Response.status(status)
                .entity(result)
                .build();
    }

    private int geosearchHealth() {
        Response response = null;
        int statusCode;
        try {
            response = geoHealthTarget.request().get();
            response.close();
            statusCode = response.getStatus();
        } catch (ProcessingException ex) {
            statusCode = 0;
            LOG.warn("Failed to fetch geosearch status", ex);
        }
        return statusCode;
    }

}
