package scot.mygov.housing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.rpz.RPZService;
import scot.mygov.housing.rpz.RPZResult;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HousingResource {

    private static final Logger LOG = LoggerFactory.getLogger(Healthcheck.class);


    private final RPZService rpzService;

    @Inject
    HousingResource(RPZService rpzService) {
        this.rpzService = rpzService;
    }

    private enum PARAM {
        POSTCODE("postcode"), DATE("date");

        private final String param;

        PARAM(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }

    @GET
    @Path("rpz")
    @Produces("application/json")
    public Response rpz(@Context UriInfo uriInfo) {

        ValidationResult validationResult = validateParams(uriInfo.getQueryParameters());
        if (!validationResult.getErrors().isEmpty()) {
            return Response.status(400).entity(validationResult).build();
        }

        String postcode =  uriInfo.getQueryParameters().getFirst(PARAM.POSTCODE.getParam());
        String dateString =  uriInfo.getQueryParameters().getFirst(PARAM.DATE.getParam());
        LocalDate date = LocalDate.parse(dateString);
        RPZResult result = rpzService.rpz(postcode, date);
        return Response.status(200).entity(result).build();
    }

    private ValidationResult validateParams(MultivaluedMap<String, String> params) {

        ValidationResult validationResult = new ValidationResult();

        // ensure all params are present
        Arrays.stream(PARAM.values())
                .filter(param -> !params.containsKey(param.getParam()))
                .map(PARAM::getParam)
                .forEach(invlaidParam -> validationResult.error(invlaidParam, "Missing required param"));

        if (params.containsKey(PARAM.DATE.getParam())) {
            String value = params.getFirst(PARAM.DATE.getParam());
            try {
                LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                LOG.warn("Invalid date", e);
                validationResult.error(PARAM.DATE.getParam(), "Invalid date:" + value);
            }
        }

        return validationResult;
    }

    public static class ValidationResult {
        private Map<String, String> errors = new HashMap<>();

        public void error(String param, String message) {
            errors.put(param, message);
        }

        public Map<String, String> getErrors() {
            return errors;
        }
    }
}
