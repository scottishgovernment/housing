package scot.mygov.housing.forms.modeltenancy;

import com.fasterxml.jackson.databind.ObjectMapper;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.DocumentGenerationServiceException;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.Validator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

@Path("model-tenancy")
public class ModelTenancyResource {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    DocumentGenerationService<ModelTenancy> modelTenancyService;

    @Inject
    Validator<ModelTenancy> modelTenancyValidator;

    @Inject
    ModelTenancyJsonTemplateLoader jsonTemplateLoader;

    @Inject
    RecaptchaCheck recaptchaCheck;

    @GET
    @Path("template")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelTenancy modelTenancyTemplate() throws ModelTenancyServiceException {
        try {
            return jsonTemplateLoader.loadJsonTemplate();
        } catch (RuntimeException e) {
            throw new ModelTenancyServiceException("Failed to load model tenancy template", e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modelTenancyMultipart(ModelTenancy modelTenancy, @QueryParam("type") String typeParam)
            throws DocumentGenerationServiceException {
        return modelTenancyResponse(modelTenancy, typeParam);
    }

    @POST
    @Path("form")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response modelTenancyMultipart(Map<String, String> params) throws DocumentGenerationServiceException {
        ModelTenancy modelTenancy = parseModel(params.get("data"));
        return modelTenancyResponse(modelTenancy, params.get("type"));
    }

    private Response modelTenancyResponse(ModelTenancy modelTenancy, String typeParam) throws DocumentGenerationServiceException {

        if (!recaptchaCheck.verify(modelTenancy.getRecaptcha())) {
            // reject it ... what is the right response to send?
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed recaptcha check").build();
        }

        DocumentType type = DocumentType.determineDocumentType(typeParam);
        modelTenancyValidator.validate(modelTenancy);
        byte[] tenancyBytes = modelTenancyService.save(modelTenancy, type);
        return Response.ok(tenancyBytes)
                .header("Content-Type", type.getContentType() )
                .header("Content-Disposition", contentDisposition(type))
                .build();
    }

    private String contentDisposition(DocumentType type) {
        return String.format("attachment; filename=\"tenancy.%s\"", type.getExtension());
    }

    private ModelTenancy parseModel(String data) throws DocumentGenerationServiceException {
        try {
            return objectMapper.readValue(data, ModelTenancy.class);
        } catch (IOException ex) {
            throw new DocumentGenerationServiceException("Could not parse model data", ex);
        }
    }
}
