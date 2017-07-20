package scot.mygov.housing.modeltenancy;

import com.fasterxml.jackson.databind.ObjectMapper;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.Validator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Map;

@Path("model-tenancy")
public class ModelTenancyResource {

    @Inject
    ModelTenancyService modelTenancyService;

    @Inject
    Validator<ModelTenancy> modelTenancyValidator;

    @GET
    @Path("template")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelTenancy modelTenancyTemplate(@Context UriInfo uriInfo) throws ModelTenancyServiceException {
        return modelTenancyService.getModelTenancyTemplate();
    }

    @POST
    @Path("form")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/pdf")
    public Response modelTenancyMultipart(Map<String, String> params) throws ModelTenancyServiceException {
        ModelTenancy modelTenancy = parseModel(params.get("data"));
        return modelTenancyResponse(modelTenancy);
    }

    private Response modelTenancyResponse(ModelTenancy modelTenancy) throws ModelTenancyServiceException {
        modelTenancyValidator.validate(modelTenancy);
        byte[] tenancyBytes = modelTenancyService.save(modelTenancy);
        return Response.ok(tenancyBytes)
                .header("Content-Disposition", "attachment; filename=\"tenancy.pdf\"")
                .build();
    }

    private ModelTenancy parseModel(String data) throws ModelTenancyServiceException {
        try {
            return new ObjectMapper().readValue(data, ModelTenancy.class);
        } catch (IOException ex) {
            throw new ModelTenancyServiceException("Could not parse model tenancy data", ex);
        }
    }

}
