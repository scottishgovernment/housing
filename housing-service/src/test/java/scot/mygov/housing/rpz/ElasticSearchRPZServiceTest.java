package scot.mygov.housing.rpz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import scot.mygov.housing.europa.AddressResultWrapper;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.europa.EuropaAddress;
import scot.mygov.housing.europa.EuropaException;
import scot.mygov.housing.europa.EuropaResults;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;


import static java.time.LocalDate.now;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ElasticSearchRPZServiceTest {

    @Test(expected = RPZServiceException.class)
    public void europaNotFoundExceptionWrappedAsExpected() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(exceptionThrowingEuropa(), anyTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test(expected = RPZServiceException.class)
    public void europaExceptionWrappedAsExpected() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(exceptionThrowingEuropaUnexpectedError(), anyTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test(expected = RPZServiceException.class)
    public void europaWithNoResultsThrowsException() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(noResultsEuropa(), anyTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test(expected = RPZServiceException.class)
    public void processingExceptionFromTargetWrappedAsExpected() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validEuropa(), exceptionThrowingTarget(new ProcessingException("blah")));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception

    }

    @Test(expected = RPZServiceException.class)
    public void webApplicationExceptionExceptionFromTargetWrappedAsExpected() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validEuropa(), exceptionThrowingTarget(new WebApplicationException("blah")));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception

    }

    @Test(expected = RPZServiceException.class)
    public void nullResultFromElasticsearchThrownAsException() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validEuropa(), targetWithObjectNode(null));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception
    }

    @Test(expected = RPZServiceException.class)
    public void resultWithNoHitsFromElasticsearchThrownAsException() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validEuropa(), targetWithObjectNode(JsonNodeFactory.instance.objectNode()));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception
    }

    @Test
    public void notHitsReturnsExpectedResult() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validEuropa(), noHitTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test
    public void hitReturnsExpectedResult() throws RPZServiceException, EuropaException, IOException {
        // ARRANGE
        double expectedMaxIncrease = 1.5;
        String expectedTitle = "title";
        RPZService service = new ElasticSearchRPZService(validEuropa(), hitTarget(expectedMaxIncrease, expectedTitle));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT
        assertTrue(actual.isInRentPressureZone());
        assertEquals(expectedMaxIncrease, actual.getMaxIncrease(), 0);
        assertEquals(expectedTitle, actual.getRentPressureZoneTitle());
    }

    private Europa noResultsEuropa() throws EuropaException, RPZServiceException {
        Europa europa = mock(Europa.class);
        when(europa.lookupUprn(any())).thenReturn(emptyResults());
        return europa;
    }

    private Europa validEuropa() throws EuropaException, RPZServiceException {
        Europa europa = mock(Europa.class);
        when(europa.lookupUprn(any())).thenReturn(validResults());
        return europa;
    }

    private EuropaResults validResults() {
        return europaResults("uprn", "EH112SW");
    }

    private EuropaResults europaResults(String uprn, String postcode) {
        EuropaResults results = new EuropaResults();
        AddressResultWrapper addressResultWrapper = new AddressResultWrapper();
        EuropaAddress address = new EuropaAddress();
        addressResultWrapper.setAddress(Collections.singletonList(address));
        results.getResults().add(addressResultWrapper);
        return results;
    }

    private EuropaResults emptyResults() {
        EuropaResults results = new EuropaResults();
        results.setResults(emptyList());
        return results;
    }

    private Europa exceptionThrowingEuropa() throws EuropaException {
        Europa europa = mock(Europa.class);
        when(europa.lookupUprn(any())).thenThrow(new EuropaException("Failed to lookup uprn", new RuntimeException("")));
        when(europa.lookupPostcode(any())).thenThrow(new EuropaException("Failed to lookup postcode", new RuntimeException("")));
        return europa;
    }

    private Europa exceptionThrowingEuropaUnexpectedError() throws EuropaException {
        Europa europa = mock(Europa.class);
        when(europa.lookupUprn(any())).thenThrow(new EuropaException("Unexpected error", new RuntimeException("")));
        when(europa.lookupPostcode(any())).thenThrow(new EuropaException("Unexpected error", new RuntimeException("")));
        return europa;
    }

    private WebTarget anyTarget() {
        return mock(WebTarget.class);
    }

    private WebTarget exceptionThrowingTarget(Exception e) {
        WebTarget target = mock(WebTarget.class);
        when(target.request()).thenThrow(e);
        return target;
    }

    private WebTarget targetWithObjectNode(ObjectNode objectNode) {
        WebTarget target = mock(WebTarget.class);
        Response response = mock(Response.class);
        when(response.readEntity(eq(ObjectNode.class))).thenReturn(objectNode);

        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(builder.accept(any(MediaType.class))).thenReturn(builder);
        when(builder.post(any(Entity.class))).thenReturn(response);

        when(target.request()).thenReturn(builder);

        return target;
    }

    private WebTarget noHitTarget() throws IOException {
        ObjectNode noHit = new ObjectMapper().readValue(this.getClass().getResourceAsStream("/noHit.json"), ObjectNode.class);
        return targetWithObjectNode(noHit);
    }

    private WebTarget hitTarget(double maxIncrease, String title) throws IOException {
        ObjectNode hit = new ObjectMapper().readValue(this.getClass().getResourceAsStream("/hit.json"), ObjectNode.class);
        ObjectNode _source = ((ObjectNode) hit.get("hits").get("hits").get(0).get("_source"));
        _source.put("maxIncrease", maxIncrease);
        _source.put("name", title);
        return targetWithObjectNode(hit);
    }

    private LocalDate anyDate() {
        return now();
    }

}
