package scot.mygov.housing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.modeltenancy.ModelTenancyService;
import scot.mygov.housing.modeltenancy.ModelTenancyServiceException;
import scot.mygov.validation.ValidationException;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.rpz.RPZService;
import scot.mygov.housing.rpz.RPZResult;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.Validator;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

@Path("/")
public class HousingResource {

    private static final Logger LOG = LoggerFactory.getLogger(Healthcheck.class);

    private final RPZService rpzService;

    @Inject
    HousingResource(RPZService rpzService) {
        this.rpzService = rpzService;
    }

    @Inject
    ModelTenancyService modelTenancyService;

    @Inject
    Validator<ModelTenancy> modelTenancyValidator;

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

        ValidationResults validationResult = validateParams(uriInfo.getQueryParameters());
        if (!validationResult.getIssues().isEmpty()) {
            return Response.status(400).entity(validationResult).build();
        }

        String postcode =  uriInfo.getQueryParameters().getFirst(PARAM.POSTCODE.getParam());
        String dateString =  uriInfo.getQueryParameters().getFirst(PARAM.DATE.getParam());
        LocalDate date = LocalDate.parse(dateString);
        RPZResult result = rpzService.rpz(postcode, date);
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("modeltenancy/raw")
    @Produces("application/pdf")
    public Response modelTenancyRaw(ModelTenancy modelTenancy, StreamingOutput streamingOutput) throws ModelTenancyServiceException {
        modelTenancyValidator.validate(modelTenancy);
        byte[] tenancyBytes = modelTenancyService.save(modelTenancy);
        return Response.ok(tenancyBytes).build();
    }

    @POST
    @Path("modeltenancy")
    @Produces("application/json")
    public Response modelTenancy(ModelTenancy modelTenancy, StreamingOutput streamingOutput) {

        try {
            modelTenancyValidator.validate(modelTenancy);
            byte [] tenancyBytes = modelTenancyService.save(modelTenancy);
            return Response.ok(new Result(tenancyBytes)).build();
        } catch (ValidationException e) {
            LOG.error("Invalid model tenancy", e);
            return Response.status(400).entity(e.getIssues()).build();
        } catch (Exception e) {
            LOG.error("Failed to produce model tenancy pdf", e);
            return Response.serverError().build();
        }
    }

    private class Result {
        private byte[] body;

        public Result(byte[] body) {
            this.body = body;
        }

        public byte[] getBody() {
            return body;
        }
    }

    private ValidationResults validateParams(MultivaluedMap<String, String> params) {

        ValidationResultsBuilder resultBuilder = new ValidationResultsBuilder();

        // ensure all params are present
        Arrays.stream(PARAM.values())
                .filter(param -> !params.containsKey(param.getParam()))
                .map(PARAM::getParam)
                .forEach(invlaidParam -> resultBuilder.issue(invlaidParam, "Missing required param"));

        if (params.containsKey(PARAM.DATE.getParam())) {
            String value = params.getFirst(PARAM.DATE.getParam());
            try {
                LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                LOG.warn("Invalid date", e);
                resultBuilder.issue(PARAM.DATE.getParam(), "Invalid date:" + value);
            }
        }

        return resultBuilder.build();
    }
}
