package scot.mygov.housing;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

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
