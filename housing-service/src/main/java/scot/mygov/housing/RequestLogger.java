package scot.mygov.housing;

import org.apache.commons.lang3.time.StopWatch;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

public class RequestLogger implements ContainerRequestFilter {

    @Inject
    public RequestLogger() {
        // Default constructor
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println(requestContext.getUriInfo());
        StopWatch stopwatch = new StopWatch();
        requestContext.setProperty("stopwatch", stopwatch);
        stopwatch.start();
    }
}
