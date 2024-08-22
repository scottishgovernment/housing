package scot.mygov.housing;

import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import javax.inject.Inject;

public class NotSupportedExceptionHandler implements ExceptionMapper<NotSupportedException> {

    @Inject
    public NotSupportedExceptionHandler() {
        // Default constructor
    }

    @Override
    public Response toResponse(NotSupportedException e) {
        Response.Status status = Response.Status.UNSUPPORTED_MEDIA_TYPE;
        String msg = String.format("<html><body><h1>%d - %s</h1></html></body>", status.getStatusCode(), status.getReasonPhrase());
        return Response
                .status(status)
                .type(MediaType.TEXT_HTML)
                .entity(msg)
                .build();
    }

}
