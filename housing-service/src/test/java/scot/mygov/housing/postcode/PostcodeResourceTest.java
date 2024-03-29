package scot.mygov.housing.postcode;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.specimpl.ResteasyUriInfo;
import org.jboss.resteasy.spi.ResteasyUriBuilder;
import org.junit.Test;
import scot.mygov.validation.ValidationResults;

import java.net.URI;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostcodeResourceTest {

    @Test
    public void missingPoscodeParamIsRejected() {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(anyService());

        // ACT
        Response actual = sut.lookup(uriInfoWithNoPostcodeParam());

        // ASSERT
        ValidationResults validationResults = (ValidationResults) actual.getEntity();
        assertTrue(validationResults.getIssues().containsKey("postcode"));
    }

    @Test
    public void emptyPostcodeParamIsRejected() {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(anyService());

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(""));

        // ASSERT
        ValidationResults validationResults = (ValidationResults) actual.getEntity();
        assertTrue(validationResults.getIssues().containsKey("postcode"));
    }

    @Test
    public void invalidPostcodeParamIsRejected() {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(anyService());

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam("invalid"));

        // ASSERT
        ValidationResults validationResults = (ValidationResults) actual.getEntity();
        assertTrue(validationResults.getIssues().containsKey("postcode"));
    }

    @Test
    public void returnsResultsFromService() throws PostcodeServiceException {
        // ARRANGE
        PostcodeServiceResults expectedResults = greenpathResults();
        PostcodeResource sut = new PostcodeResource(serviceWithResults(expectedResults));

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(scottishPostcode()));

        // ASSERT
        assertEquals(actual.getStatus(), 200);
        PostcodeServiceResults actualResults = (PostcodeServiceResults) actual.getEntity();
        assertResults(expectedResults, actualResults);
    }

    @Test
    public void returnsResultsFromServicePostcodeHasSpace() throws PostcodeServiceException {
        // ARRANGE
        PostcodeServiceResults expectedResults = greenpathResults();
        PostcodeResource sut = new PostcodeResource(serviceWithResults(expectedResults));

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(scottishPostcodeWithSpace()));

        // ASSERT
        assertEquals(actual.getStatus(), 200);
        PostcodeServiceResults actualResults = (PostcodeServiceResults) actual.getEntity();
        assertResults(expectedResults, actualResults);
    }

    @Test
    public void returnsEmptyListIfNoResultsFromService() throws PostcodeServiceException {
        // ARRANGE
        PostcodeServiceResults expectedResults = emptyResults();
        PostcodeResource sut = new PostcodeResource(serviceWithResults(expectedResults));

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(scottishPostcode()));

        // ASSERT
        assertEquals(actual.getStatus(), 200);
        PostcodeServiceResults actualResults = (PostcodeServiceResults) actual.getEntity();
        assertResults(expectedResults, actualResults);
    }

    @Test
    public void exceptionFromServiceReturns503() throws PostcodeServiceException {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(exceptionThrowingService());

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(scottishPostcode()));

        // ASSERT
        assertEquals(actual.getStatus(), 503);
    }

    private void assertResults(PostcodeServiceResults expected, PostcodeServiceResults actual) {
        assertEquals(expected.getResults(), actual.getResults());
    }

    private PostcodeService anyService() {
        return mock(PostcodeService.class);
    }

    private PostcodeService serviceWithResults(PostcodeServiceResults results) throws PostcodeServiceException {
        PostcodeService service = mock(PostcodeService.class);
        when(service.lookup(any())).thenReturn(results);
        return service;
    }

    private PostcodeService exceptionThrowingService() throws PostcodeServiceException {
        PostcodeService service = mock(PostcodeService.class);
        when(service.lookup(any())).thenThrow(new PostcodeServiceException("", new RuntimeException("")));
        return service;
    }

    private PostcodeServiceResults greenpathResults() {
        PostcodeServiceResults results = new PostcodeServiceResults();
        results.setResults(singletonList(anyResult()));
        return results;
    }

    private PostcodeServiceResults emptyResults() {
        PostcodeServiceResults results = new PostcodeServiceResults();
        results.setResults(emptyList());
        return results;
    }

    private PostcodeServiceResult anyResult() {
        PostcodeServiceResult result = new PostcodeServiceResult();
        result.setUprn("uprn");
        result.setPostcode("EH10 4AX");
        result.setTown("Edinburgh");
        return result;
    }

    private UriInfo uriInfoWithNoPostcodeParam() {
        URI uri = UriBuilder.newInstance().build();
        return new ResteasyUriInfo(uri);
    }

    private UriInfo uriInfoWithPostcodeParam(String postcode) {
        URI uri = UriBuilder.newInstance()
                .queryParam("postcode", postcode).build();
        return new ResteasyUriInfo(uri);
    }

    private String scottishPostcode() {
        return "EH104AX";
    }

    private String scottishPostcodeWithSpace() {
        return "EH10 4AX";
    }

}
