package scot.mygov.housing;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.apache.commons.lang3.time.StopWatch;

import javax.inject.Inject;
import java.io.IOException;

public class RequestLogger implements ContainerRequestFilter {

    @Inject
    public RequestLogger() {
        // Default constructor
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        StopWatch stopwatch = new StopWatch();
        requestContext.setProperty("stopwatch", stopwatch);
        stopwatch.start();
    }
}
