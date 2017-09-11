package scot.mygov.housing.modeltenancy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
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

    private static final Logger LOG = LoggerFactory.getLogger(ModelTenancyResource.class);

    @Inject
    ModelTenancyService modelTenancyService;

    @Inject
    Validator<ModelTenancy> modelTenancyValidator;

    @GET
    @Path("template")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelTenancy modelTenancyTemplate() throws ModelTenancyServiceException {
        return modelTenancyService.getModelTenancyTemplate();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modelTenancyMultipart(ModelTenancy modelTenancy, @QueryParam("type") String typeParam) throws ModelTenancyServiceException {
        return modelTenancyResponse(modelTenancy, typeParam);
    }

    @POST
    @Path("form")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response modelTenancyMultipart(Map<String, String> params) throws ModelTenancyServiceException {
        ModelTenancy modelTenancy = parseModel(params.get("data"));
        return modelTenancyResponse(modelTenancy, params.get("type"));
    }

    private Response modelTenancyResponse(ModelTenancy modelTenancy, String typeParam) throws ModelTenancyServiceException {
        DocumentType type = determineDocumentType(typeParam);
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

    private ModelTenancy parseModel(String data) throws ModelTenancyServiceException {
        try {
            return new ObjectMapper().readValue(data, ModelTenancy.class);
        } catch (IOException ex) {
            throw new ModelTenancyServiceException("Could not parse model tenancy data", ex);
        }
    }

    private DocumentType determineDocumentType(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return DocumentType.PDF;
        }
        try {
            return DocumentType.valueOf(contentType);
        } catch (IllegalArgumentException e) {
            LOG.warn("Unrecognised document type: " + contentType, e);
            // default to PDF if the param is invalid
            return DocumentType.PDF;
        }
    }
}
