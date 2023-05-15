package scot.mygov.housing;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class ResponseLogger implements ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Housing.class);

    @Inject
    public ResponseLogger() {
        // Default constructor
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        String method = request.getRequest().getMethod();
        String path = request.getUriInfo().getPath();
        int status = response.getStatus();

        StopWatch stopWatch = (StopWatch) request.getProperty("stopwatch");
        if (stopWatch != null && stopWatch.isStarted()) {
            stopWatch.stop();
            LOGGER.info("{} {} {} {}", status, method, path, stopWatch.getTime());
        } else {
            LOGGER.info("{} {} {}", status, method, path);
        }
    }

}
