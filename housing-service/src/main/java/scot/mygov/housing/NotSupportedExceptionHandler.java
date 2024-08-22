package scot.mygov.housing;

import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import javax.inject.Inject;

public class NotSupportedExceptionHandler implements ExceptionMapper<NotSupportedException> {

    private static final String ERROR = "<html><body><h1>406 - Not Acceptable</h1></html></body>";

    @Inject
    public NotSupportedExceptionHandler() {
        // Default constructor
    }

    @Override
    public Response toResponse(NotSupportedException e) {
        return Response
                .status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                .type(MediaType.TEXT_HTML)
                .entity(ERROR)
                .build();
    }

}
