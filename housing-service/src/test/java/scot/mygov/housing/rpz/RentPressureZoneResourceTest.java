package scot.mygov.housing.rpz;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.specimpl.ResteasyUriInfo;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.validation.ValidationResults;

import java.net.URI;
import java.time.LocalDate;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RentPressureZoneResourceTest {


    @Test
    public void serviceExceptionReturnServerError() throws RPZServiceException {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(exceptionThrowingRpzService());
        URI uri = UriBuilder.newInstance()
                .queryParam("uprn", "validuprn")
                .queryParam("date", "2012-10-10")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(503, response.getStatus());
    }

    @Test
    public void clientExceptionReturnClientError() throws RPZServiceException {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(clientExceptionThrowingRpzService());
        URI uri = UriBuilder.newInstance()
                .queryParam("uprn", "validuprn")
                .queryParam("date", "2012-10-10")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(400, response.getStatus());
    }

    @Test
    public void missingUprnReturnsError() {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(null);
        URI uri = UriBuilder.newInstance().queryParam("date", "2010-01-01").build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(400, response.getStatus());
        ValidationResults validationResult = (ValidationResults) response.getEntity();
        assertTrue(validationResult.getIssues().containsKey("uprn"));
    }

    @Test
    public void missingDateReturnsError() {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(null);
        URI uri = UriBuilder.newInstance().queryParam("uprn", "EH104AX").build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(400, response.getStatus());
        ValidationResults validationResult = (ValidationResults) response.getEntity();
        assertTrue(validationResult.getIssues().containsKey("date"));
    }

    @Test
    public void invalidDateReturnsError() {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(null);
        URI uri = UriBuilder.newInstance()
                .queryParam("uprn", "EH104AX")
                .queryParam("date", "111")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(400, response.getStatus());
        ValidationResults validationResult = (ValidationResults) response.getEntity();
        assertTrue(validationResult.getIssues().containsKey("date"));
    }

    @Test
    public void returnsResultFromService() {

        // ARRANGE
        double expectedMaxIncrease = 100;
        String expectedTitle = "title";
        RPZResult result = new RPZResult.Builder()
                .inRentPressureZone(true)
                .title(expectedTitle)
                .maxIncrease(expectedMaxIncrease).build();
        RPZService service = rpzService(result);
        RentPressureZoneResource sut = new RentPressureZoneResource(service);
        URI uri = UriBuilder.newInstance()
                .queryParam("uprn", "validuprn")
                .queryParam("date", "2012-10-10")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(RPZResult.class, response.getEntity().getClass());
        assertEquals(200, response.getStatus());
        RPZResult rpzResult = (RPZResult) response.getEntity();
        assertTrue(rpzResult.isInRentPressureZone());
        assertEquals(expectedTitle, rpzResult.getRentPressureZoneTitle());
        assertEquals(expectedMaxIncrease, rpzResult.getMaxIncrease(), 0);
    }

    public RPZService exceptionThrowingRpzService() throws RPZServiceException {
        RPZService service = Mockito.mock(RPZService.class);
        when(service.rpz(any(), any())).thenThrow(new RPZServiceException(""));
        return service;
    }

    public RPZService clientExceptionThrowingRpzService() throws RPZServiceException {
        RPZService service = Mockito.mock(RPZService.class);
        when(service.rpz(any(), any())).thenThrow(new RPZServiceClientException(emptyMap()));
        return service;
    }

    public RPZService rpzService(RPZResult result) {
        return new RPZService() {
            public RPZResult rpz(String uprn, LocalDate date) {
                return result;
            }
        };
    }
}
