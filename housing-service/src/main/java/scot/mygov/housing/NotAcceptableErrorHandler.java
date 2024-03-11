package scot.mygov.housing;

import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import javax.inject.Inject;

public class NotAcceptableErrorHandler implements ExceptionMapper<NotAcceptableException> {

    private static final String ERROR = "<html><body><h1>406 - Not Acceptable</h1></html></body>";

    @Inject
    public NotAcceptableErrorHandler() {
        // Default constructor
    }

    @Override
    public Response toResponse(NotAcceptableException e) {
        return Response
                .status(Response.Status.NOT_ACCEPTABLE)
                .type(MediaType.TEXT_HTML)
                .entity(ERROR)
                .build();
    }

}
