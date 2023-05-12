package scot.mygov.housing.rpz;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.Healthcheck;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

@Path("rpz")
public class RentPressureZoneResource {

    private static final Logger LOG = LoggerFactory.getLogger(Healthcheck.class);

    private final RPZService rpzService;

    @Inject
    RentPressureZoneResource(RPZService rpzService) {
        this.rpzService = rpzService;
    }

    private enum RPZ_PARAM {
        UPRN("uprn"), DATE("date");

        private final String param;

        RPZ_PARAM(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response rpz(@Context UriInfo uriInfo) {

        ValidationResults validationResult = validateRPZParams(uriInfo.getQueryParameters());
        if (!validationResult.getIssues().isEmpty()) {
            return Response.status(400).entity(validationResult).build();
        }

        String uprn =  uriInfo.getQueryParameters().getFirst(RPZ_PARAM.UPRN.getParam());
        String dateString =  uriInfo.getQueryParameters().getFirst(RPZ_PARAM.DATE.getParam());
        LocalDate date = LocalDate.parse(dateString);
        try {
            RPZResult result = rpzService.rpz(uprn, date);
            return Response.status(200).entity(result).build();
        } catch (RPZServiceClientException e) {
            LOG.error("Failed to get RPZResult:", e);
            ValidationResultsBuilder resultBuilder = new ValidationResultsBuilder();
            e.getErrors().entrySet().stream().forEach(entry -> resultBuilder.issue(entry.getKey(), entry.getValue()));
            return Response.status(400).entity(resultBuilder.build()).build();
        } catch (RPZServiceException e) {
            LOG.error("Failed to get RPZResult", e);
            return Response.status(503).entity("RPZ data not available").build();
        }
    }

    private ValidationResults validateRPZParams(MultivaluedMap<String, String> params) {

        ValidationResultsBuilder resultBuilder = new ValidationResultsBuilder();

        // ensure all params are present
        Arrays.stream(RPZ_PARAM.values())
                .filter(param -> !params.containsKey(param.getParam()))
                .map(RPZ_PARAM::getParam)
                .forEach(invlaidParam -> resultBuilder.issue(invlaidParam, "Missing required param"));

        if (params.containsKey(RPZ_PARAM.DATE.getParam())) {
            String value = params.getFirst(RPZ_PARAM.DATE.getParam());
            try {
                LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                LOG.warn("Invalid date", e);
                resultBuilder.issue(RPZ_PARAM.DATE.getParam(), "Invalid date:" + value);
            }
        }

        return resultBuilder.build();
    }
}
