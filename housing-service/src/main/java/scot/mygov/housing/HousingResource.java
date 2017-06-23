package scot.mygov.housing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.modeltenancy.ModelTenancyService;
import scot.mygov.housing.modeltenancy.ModelTenancyServiceException;
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

    private enum RPZ_PARAM {
        POSTCODE("postcode"), DATE("date");

        private final String param;

        RPZ_PARAM(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }

    @GET
    @Path("modeltenancy/template")
    @Produces("application/json")
    public Response modelTenancyTmeplate(@Context UriInfo uriInfo) throws ModelTenancyServiceException {
        ModelTenancy modelTenancyTemplate = modelTenancyService.getModelTenancytemplate();
        return Response.status(200).entity(modelTenancyTemplate).build();
    }

    @POST
    @Path("modeltenancy")
    @Produces("application/pdf")
    public Response modelTenancyRaw(ModelTenancy modelTenancy, StreamingOutput streamingOutput) throws ModelTenancyServiceException {
        modelTenancyValidator.validate(modelTenancy);
        byte[] tenancyBytes = modelTenancyService.save(modelTenancy);
        return Response.ok(tenancyBytes).build();
    }

    @GET
    @Path("rpz")
    @Produces("application/json")
    public Response rpz(@Context UriInfo uriInfo) {

        ValidationResults validationResult = validateRPZParams(uriInfo.getQueryParameters());
        if (!validationResult.getIssues().isEmpty()) {
            return Response.status(400).entity(validationResult).build();
        }

        String postcode =  uriInfo.getQueryParameters().getFirst(RPZ_PARAM.POSTCODE.getParam());
        String dateString =  uriInfo.getQueryParameters().getFirst(RPZ_PARAM.DATE.getParam());
        LocalDate date = LocalDate.parse(dateString);
        RPZResult result = rpzService.rpz(postcode, date);
        return Response.status(200).entity(result).build();
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
