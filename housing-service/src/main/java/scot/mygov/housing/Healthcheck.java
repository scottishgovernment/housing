package scot.mygov.housing;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

import static java.lang.String.format;

@Path("health")
@Produces(MediaType.APPLICATION_JSON)
public class Healthcheck {

    private static final Logger LOG = LoggerFactory.getLogger(Healthcheck.class);

    @Inject
    AsposeLicense asposeLicense;

    @Inject
    @Named(HousingModule.GEO_HEALTH)
    WebTarget geoHealthTarget;

    @GET
    public Response health(
            @QueryParam("licenseDays") @DefaultValue("10") int licenseDays
    ) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode result = factory.objectNode();

        ArrayNode errors = factory.arrayNode();
        ArrayNode warnings = factory.arrayNode();
        ObjectNode data = factory.objectNode();

        addGeosearchInfo(result, errors);
        addLicenseInfo(result, errors, warnings, data, licenseDays);

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

    private void addGeosearchInfo(ObjectNode result, ArrayNode errors) {
        boolean geosearchOK = geosearchHealth();
        result.put("geosearch", geosearchOK);
        if (!geosearchOK) {
            errors.add("Geosearch is unavailable");
        }
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

    private boolean geosearchHealth() {
        try {
            Response response = geoHealthTarget.request().get();
            response.close();
            return response.getStatus() == 200;
        } catch (ProcessingException ex) {
            LOG.info("Failed to fetch geosearch status", ex);
            return false;
        }
    }

}
