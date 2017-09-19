package scot.mygov.housing.rpz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.MapcloudResult;
import scot.mygov.housing.mapcloud.MapcloudResults;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDate;


import static java.time.LocalDate.now;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ElasticSearchRPZServiceTest {

    @Test(expected = RPZServiceException.class)
    public void mapCloudNotFoundExceptionWrappedAsExpected() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(exceptionThrowingMapcloud(), anyTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test(expected = RPZServiceException.class)
    public void mapCloudExceptionWrappedAsExpected() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(exceptionThrowingMapcloudUnexpectedError(), anyTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test(expected = RPZServiceException.class)
    public void mapCloudWithNoResultsThrowsException() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(noResultsMapcloud(), anyTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test(expected = RPZServiceException.class)
    public void processingExceptionFromTargetWrappedAsExpected() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validMapcloud(), exceptionThrowingTarget(new ProcessingException("blah")));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception

    }

    @Test(expected = RPZServiceException.class)
    public void webApplicationExceptionExceptionFromTargetWrappedAsExpected() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validMapcloud(), exceptionThrowingTarget(new WebApplicationException("blah")));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception

    }

    @Test(expected = RPZServiceException.class)
    public void nullResultFromElasticsearchThrownAsException() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validMapcloud(), targetWithObjectNode(null));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception
    }

    @Test(expected = RPZServiceException.class)
    public void resultWithNoHitsFromElasticsearchThrownAsException() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validMapcloud(), targetWithObjectNode(JsonNodeFactory.instance.objectNode()));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT -- see expected exception
    }

    @Test
    public void notHitsReturnsExpectedResult() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        RPZService service = new ElasticSearchRPZService(validMapcloud(), noHitTarget());

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test
    public void hitReturnsExpectedResult() throws RPZServiceException, MapcloudException, IOException {
        // ARRANGE
        double expectedMaxIncrease = 1.5;
        String expectedTitle = "title";
        RPZService service = new ElasticSearchRPZService(validMapcloud(), hitTarget(expectedMaxIncrease, expectedTitle));

        // ACT
        RPZResult actual = service.rpz("anyUprn", anyDate());

        // ASSERT
        assertTrue(actual.isInRentPressureZone());
        assertEquals(expectedMaxIncrease, actual.getMaxIncrease(), 0);
        assertEquals(expectedTitle, actual.getRentPressureZoneTitle());
    }

    private Mapcloud noResultsMapcloud() throws MapcloudException, RPZServiceException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupUprn(any())).thenReturn(emptyResults());
        return mapcloud;
    }

    private Mapcloud validMapcloud() throws MapcloudException, RPZServiceException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupUprn(any())).thenReturn(validResults());
        return mapcloud;
    }

    private MapcloudResults validResults() {
        return mapcloudResults("uprn", "EH112SW");
    }

    private MapcloudResults mapcloudResults(String uprn, String postcode) {
        MapcloudResults results = new MapcloudResults();
        MapcloudResult result = new MapcloudResult();
        result.setUprn(uprn);
        result.setPostcode(postcode);
        results.setResults(singletonList(result));
        return results;
    }

    private MapcloudResults emptyResults() {
        MapcloudResults results = new MapcloudResults();
        results.setResults(emptyList());
        return results;
    }

    private Mapcloud exceptionThrowingMapcloud() throws MapcloudException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupUprn(any())).thenThrow(new MapcloudException("Failed to lookup uprn", new RuntimeException("")));
        when(mapcloud.lookupPostcode(any())).thenThrow(new MapcloudException("Failed to lookup postcode", new RuntimeException("")));
        return mapcloud;
    }

    private Mapcloud exceptionThrowingMapcloudUnexpectedError() throws MapcloudException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupUprn(any())).thenThrow(new MapcloudException("Unexpected error", new RuntimeException("")));
        when(mapcloud.lookupPostcode(any())).thenThrow(new MapcloudException("Unexpected error", new RuntimeException("")));
        return mapcloud;
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
