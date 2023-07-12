package scot.mygov.housing;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

public class NotFoundErrorHandler implements ExceptionMapper<NotFoundException> {

    private static final String NOT_FOUND_HTML = "<html><body><h1>404 - Not Found</h1></html></body>";

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(NOT_FOUND)
                .type(MediaType.TEXT_HTML)
                .entity(NOT_FOUND_HTML)
                .build();
    }

}
