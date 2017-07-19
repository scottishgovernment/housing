package scot.mygov.housing.modeltenancy;

import com.fasterxml.jackson.databind.ObjectMapper;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.Validator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("/")
public class ModelTenancyResource {

    @Inject
    ModelTenancyService modelTenancyService;

    @Inject
    Validator<ModelTenancy> modelTenancyValidator;

    @GET
    @Path("modeltenancy/template")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modelTenancyTemplate(@Context UriInfo uriInfo) throws ModelTenancyServiceException {
        ModelTenancy modelTenancyTemplate = modelTenancyService.getModelTenancytemplate();
        return Response.status(200).entity(modelTenancyTemplate).build();
    }

    @POST
    @Path("modeltenancy")
    @Produces("application/pdf")
    public Response modelTenancyRaw(ModelTenancy modelTenancy) throws ModelTenancyServiceException {
        modelTenancyValidator.validate(modelTenancy);
        byte[] tenancyBytes = modelTenancyService.save(modelTenancy);
        return Response.ok(tenancyBytes)
                .header("Content-Disposition", "attachment; filename=\"tenancy.pdf\"")
                .build();
    }

    @POST
    @Path("model-tenancy/form")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/pdf")
    public Response modelTenancyForm(@FormParam("data") String data)
            throws ModelTenancyServiceException {
        try {
            ModelTenancy modelTenancy = new ObjectMapper().readValue(data, ModelTenancy.class);
            return modelTenancyRaw(modelTenancy);
        } catch (IOException ex) {
            throw new ModelTenancyServiceException("Could not parse model tenancy data", ex);
        }
    }

}
