package scot.mygov.housing.cpi;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.specimpl.ResteasyUriInfo;
import org.junit.Test;
import scot.mygov.validation.ValidationResults;

import java.net.URI;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CPIResourceTest {

    @Test
    public void cpiDeltaReturnsDataFromServiceForGreenpath() throws CPIServiceException {
        // ARRANGE
        CPIService service = cpiService(100.0);
        CPIResource sut = new CPIResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("from_date", "2010-01-01")
                .queryParam("to_date", "2010-02-02")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.cpiDelta(uriInfo);

        // ASSERT
        assertEquals(Double.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 200);
        Double result = (Double) response.getEntity();
        assertEquals(result, 100.00, 0.0);
    }

    @Test
    public void cpiDeltaRejectsMissingFromDate() throws CPIServiceException {
        // ARRANGE
        CPIService service = cpiService(100.0);
        CPIResource sut = new CPIResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("to_date", "2010-02-02")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.cpiDelta(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void cpiDeltaRejectsMissingToDate() throws CPIServiceException {
        // ARRANGE
        CPIService service = cpiService(100.0);
        CPIResource sut = new CPIResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("from_date", "2010-02-02")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.cpiDelta(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void cpiDeltaRejectsInvalidFromDate() throws CPIServiceException {
        // ARRANGE
        CPIService service = cpiService(100.0);
        CPIResource sut = new CPIResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("from_date", "blah")
                .queryParam("to_date", "2010-02-02")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.cpiDelta(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void cpiDeltaRejectsInvalidToDate() throws CPIServiceException {
        // ARRANGE
        CPIService service = cpiService(100.0);
        CPIResource sut = new CPIResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("to_date", "blah")
                .queryParam("from_date", "2010-02-02")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.cpiDelta(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void cpiDeltaRejectsFromDateBeforeToDate() throws CPIServiceException {
        // ARRANGE
        CPIService service = cpiService(100.0);
        CPIResource sut = new CPIResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("to_date", "2000-01-01")
                .queryParam("from_date", "2010-02-02")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.cpiDelta(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void cpiDeltaReturns503ForException() throws CPIServiceException {
        // ARRANGE
        CPIService service = exceptionThrowingCpiService();
        CPIResource sut = new CPIResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("from_date", "2000-01-01")
                .queryParam("to_date", "2010-02-02")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.cpiDelta(uriInfo);

        // ASSERT
        assertEquals(String.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 503);
    }
    private CPIService cpiService(double result) throws CPIServiceException {

        CPIService cpiService = mock(CPIService.class);
        when(cpiService.cpiDelta(any(LocalDate.class), any(LocalDate.class))).thenReturn(result);
        return cpiService;
    }

    private CPIService exceptionThrowingCpiService() throws CPIServiceException {
        CPIService cpiService = mock(CPIService.class);
        when(cpiService.cpiDelta(any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new CPIServiceException("arg!", null));
        return cpiService;
    }
}
