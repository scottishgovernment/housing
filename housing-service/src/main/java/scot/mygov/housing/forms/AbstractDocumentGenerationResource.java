package scot.mygov.housing.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import scot.mygov.documents.DocumentType;

import javax.ws.rs.*;
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

    /**
     * We think that some users press refresh after downloading the form causinf a get request.  Without this method
     * this is caught by the expcetion handler and the service alerts.
     */
    @Path("form")
    @GET
    public Response get() {
        String html = "<htlm><body>" +
                "<p>GET not supported, please return to start of form.</p>" +
                "</body></html>";
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .type(MediaType.TEXT_HTML_TYPE)
                .entity(html).build();
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
