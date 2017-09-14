package scot.mygov.housing.rpz;

import org.jboss.resteasy.specimpl.ResteasyUriBuilder;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import scot.mygov.validation.ValidationResults;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RentPressureZoneResourceTest {


    @Test
    public void serviceExceptionReturnServerError() throws RPZServiceException {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(exceptionThrowingRpzService());
        URI uri = new ResteasyUriBuilder()
                .queryParam("uprn", "validuprn")
                .queryParam("date", "2012-10-10")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(response.getStatus(), 503);
    }

    @Test
    public void clientExceptionReturnClientError() throws RPZServiceException {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(clientExceptionThrowingRpzService());
        URI uri = new ResteasyUriBuilder()
                .queryParam("uprn", "validuprn")
                .queryParam("date", "2012-10-10")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(response.getStatus(), 400);
    }

    @Test
    public void missingUprnReturnsError() {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(null);
        URI uri = new ResteasyUriBuilder().queryParam("date", "2010-01-01").build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
        ValidationResults validationResult = (ValidationResults) response.getEntity();
        assertTrue(validationResult.getIssues().containsKey("uprn"));
    }

    @Test
    public void missingDateReturnsError() {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(null);
        URI uri = new ResteasyUriBuilder().queryParam("uprn", "EH104AX").build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
        ValidationResults validationResult = (ValidationResults) response.getEntity();
        assertTrue(validationResult.getIssues().containsKey("date"));
    }

    @Test
    public void invalidDateReturnsError() {

        // ARRANGE
        RentPressureZoneResource sut = new RentPressureZoneResource(null);
        URI uri = new ResteasyUriBuilder()
                .queryParam("uprn", "EH104AX")
                .queryParam("date", "111")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(ValidationResults.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 400);
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
        URI uri = new ResteasyUriBuilder()
                .queryParam("uprn", "validuprn")
                .queryParam("date", "2012-10-10")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        assertEquals(RPZResult.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 200);
        RPZResult rpzResult = (RPZResult) response.getEntity();
        assertTrue(rpzResult.isInRentPressureZone());
        assertEquals(rpzResult.getRentPressureZoneTitle(), expectedTitle);
        assertEquals(rpzResult.getMaxIncrease(), expectedMaxIncrease, 0);
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
