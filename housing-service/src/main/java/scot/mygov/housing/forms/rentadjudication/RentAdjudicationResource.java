package scot.mygov.housing.forms.rentadjudication;


import com.fasterxml.jackson.databind.ObjectMapper;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

@Path("rent-adjudication")
public class RentAdjudicationResource {

    @Inject
    RentAdjudicationService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response multipart(RentAdjudication model, @QueryParam("type") String typeParam)
            throws RentAdjudicationServiceException {
        return response(model, typeParam);
    }

    @POST
    @Path("form")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response multipart(Map<String, String> params)
            throws RentAdjudicationServiceException {
        RentAdjudication model = parseModel(params.get("data"));
        return response(model, params.get("type"));
    }

    private Response response(RentAdjudication model, String typeParam)
            throws RentAdjudicationServiceException {
        DocumentType type = DocumentType.determineDocumentType(typeParam);
        byte[] tenancyBytes = service.save(model, type);
        return Response.ok(tenancyBytes)
                .header("Content-Type", type.getContentType() )
                .header("Content-Disposition", contentDisposition(type))
                .build();
    }

    private String contentDisposition(DocumentType type) {
        return String.format("attachment; filename=\"adjudication.%s\"", type.getExtension());
    }

    private RentAdjudication parseModel(String data) throws RentAdjudicationServiceException {
        try {
            return new ObjectMapper().readValue(data, RentAdjudication.class);
        } catch (IOException ex) {
            throw new RentAdjudicationServiceException("Could not parse model data", ex);
        }
    }
}
