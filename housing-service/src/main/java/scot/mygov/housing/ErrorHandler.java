package scot.mygov.housing;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

public class ErrorHandler implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    private Meter errorMeter;

    @Inject
    public ErrorHandler(MetricRegistry metricRegistry) {
        this.errorMeter = metricRegistry.meter(MetricName.ERROR_RATE.name(this));
    }

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error("Exception thrown", exception);
        errorMeter.mark();
        return Response.status(INTERNAL_SERVER_ERROR)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

}
