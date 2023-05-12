package scot.mygov.housing;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class ResponseLoggerTest {

    @Test
    public void hasStopwatch() throws IOException {
        ResponseLogger sut = new ResponseLogger();
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getPath()).thenReturn("path");
        ContainerRequestContext request = Mockito.mock(ContainerRequestContext.class);
        Request req = Mockito.mock(Request.class);
        Mockito.when(request.getRequest()).thenReturn(req);
        Mockito.when(request.getUriInfo()).thenReturn(uriInfo);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Mockito.when(request.getProperty(Mockito.any())).thenReturn(stopWatch);
        ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
        sut.filter(request, response);
    }

    @Test
    public void nullStopwatch() throws IOException {
        ResponseLogger sut = new ResponseLogger();
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getPath()).thenReturn("path");
        ContainerRequestContext request = Mockito.mock(ContainerRequestContext.class);
        Request req = Mockito.mock(Request.class);
        Mockito.when(request.getRequest()).thenReturn(req);
        Mockito.when(request.getUriInfo()).thenReturn(uriInfo);
        ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
        sut.filter(request, response);
    }

}
