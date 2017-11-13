package scot.mygov.housing.forms.nonprovisionofdocumentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.DocumentGenerationServiceException;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

@Path("non-provision-of-documentation")
public class NonProvisionOfDocumentationResource {

    @Inject
    DocumentGenerationService<NonProvisionOfDocumentation> service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response multipart(NonProvisionOfDocumentation model, @QueryParam("type") String typeParam)
            throws DocumentGenerationServiceException {
        return response(model, typeParam);
    }

    @POST
    @Path("form")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response multipart(Map<String, String> params)
            throws DocumentGenerationServiceException {
        NonProvisionOfDocumentation model = parseModel(params.get("data"));
        return response(model, params.get("type"));
    }

    private Response response(NonProvisionOfDocumentation model, String typeParam)
            throws DocumentGenerationServiceException {
        DocumentType type = DocumentType.determineDocumentType(typeParam);
        byte[] tenancyBytes = service.save(model, type);
        return Response.ok(tenancyBytes)
                .header("Content-Type", type.getContentType() )
                .header("Content-Disposition", contentDisposition(type))
                .build();
    }

    private String contentDisposition(DocumentType type) {
        return String.format("attachment; filename=\"non-provision-of-documentation.%s\"", type.getExtension());
    }

    private NonProvisionOfDocumentation parseModel(String data) throws DocumentGenerationServiceException {
        try {
            return new ObjectMapper().readValue(data, NonProvisionOfDocumentation.class);
        } catch (IOException ex) {
            throw new DocumentGenerationServiceException("Could not parse model data", ex);
        }
    }
}
