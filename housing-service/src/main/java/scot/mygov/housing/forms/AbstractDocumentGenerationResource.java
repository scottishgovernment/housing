package scot.mygov.housing.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.util.MultipartParser;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import scot.mygov.documents.DocumentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class AbstractDocumentGenerationResource<T extends AbstractFormModel> {

    DocumentGenerationService<T> service;

    RecaptchaCheck recaptchaCheck;

    public AbstractDocumentGenerationResource(DocumentGenerationService<T> service, RecaptchaCheck recaptchaCheck) {
        this.service = service;
        this.recaptchaCheck = recaptchaCheck;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response multipart(T model, @QueryParam("type") String typeParam)
            throws DocumentGenerationServiceException {
        return response(model, typeParam);
    }

    @Path("form")
    @POST
    @Encoded
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response multipart(@MultipartForm Map<String, String> params)
            throws DocumentGenerationServiceException {
        T model = parseModel(params.get("data"));
        return response(model, params.get("type"));
    }


    private Response response(T model, String typeParam)
            throws DocumentGenerationServiceException {

        if (!recaptchaCheck.verify(model.getRecaptcha())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed recaptcha check").build();
        }

        DocumentType type = DocumentType.determineDocumentType(typeParam);
        validate(model);
        byte[] documentBytes = service.save(model, type);
        return Response.ok(documentBytes)
                .header("Content-Type", type.getContentType() )
                .header("Content-Disposition", contentDisposition(type))
                .build();
    }

    private String contentDisposition(DocumentType type) {
        return String.format("attachment; filename=\"%s.%s\"", contentDispositionFilenameStem(), type.getExtension());
    }

    protected void validate(T model) {
        // by default do not perform any validation
    }

    protected abstract String contentDispositionFilenameStem();

    protected abstract Class<T> getModelClass();

    private T parseModel(String data) throws DocumentGenerationServiceException {
        try {
            final String decoded = URLDecoder.decode(data, StandardCharsets.UTF_8.name());
            return new ObjectMapper().<T>readValue(decoded, getModelClass());
        } catch (IOException ex) {
            throw new DocumentGenerationServiceException("Could not parse model data", ex);
        }
    }

}
