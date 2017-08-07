package scot.mygov.housing.postcode;

import org.jboss.resteasy.specimpl.ResteasyUriBuilder;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.rpz.PostcodeSource;
import scot.mygov.validation.ValidationResults;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PostcodeResourceTest {

    @Test
    public void missingPoscodeParamIsRejected() {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(anyService(), anySource());

        // ACT
        Response actual = sut.lookup(uriInfoWithNoPostcodeParam());

        // ASSERT
        ValidationResults validationResults = (ValidationResults) actual.getEntity();
        assertTrue(validationResults.getIssues().containsKey("postcode"));
    }

    @Test
    public void emptyPostcodeParamIsRejected() {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(anyService(), anySource());

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(""));

        // ASSERT
        ValidationResults validationResults = (ValidationResults) actual.getEntity();
        assertTrue(validationResults.getIssues().containsKey("postcode"));
    }

    @Test
    public void invalidPostcodeParamIsRejected() {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(anyService(), anySource());

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam("invalid"));

        // ASSERT
        ValidationResults validationResults = (ValidationResults) actual.getEntity();
        assertTrue(validationResults.getIssues().containsKey("postcode"));
    }

    @Test
    public void englishPostcodeParamIsRejected() {
        // ARRANGE
        PostcodeResource sut = new PostcodeResource(anyService(), scottishSource());

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(englishPostcode()));

        // ASSERT
        ValidationResults validationResults = (ValidationResults) actual.getEntity();
        assertTrue(validationResults.getIssues().containsKey("postcode"));
    }

    @Test
    public void returnsResultsFromService() throws PostcodeServiceException {
        // ARRANGE
        PostcodeServiceResults expectedResults = greenpathResults();
        PostcodeResource sut = new PostcodeResource(serviceWithResults(expectedResults), scottishSource());

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
        PostcodeResource sut = new PostcodeResource(exceptionThrowingService(), scottishSource());

        // ACT
        Response actual = sut.lookup(uriInfoWithPostcodeParam(scottishPostcode()));

        // ASSERT
        assertEquals(actual.getStatus(), 503);
    }

    private void assertResults(PostcodeServiceResults expected, PostcodeServiceResults actual) {
        assertEquals(expected.getResults(), actual.getResults());
    }

    private PostcodeService anyService() {
        return Mockito.mock(PostcodeService.class);
    }

    private PostcodeSource anySource() {
        return Mockito.mock(PostcodeSource.class);
    }

    private PostcodeSource scottishSource() {
        PostcodeSource source = Mockito.mock(PostcodeSource.class);
        Postcode postcode = new Postcode();
        postcode.setNormalisedPostcode(normalisedScottishPostcode());
        postcode.setPostcode(scottishPostcode());
        when(source.postcode(Mockito.eq(scottishPostcode()))).thenReturn(postcode);
        return source;
    }

    private PostcodeService serviceWithResults(PostcodeServiceResults results) throws PostcodeServiceException {
        PostcodeService service = Mockito.mock(PostcodeService.class);
        when(service.lookup(any())).thenReturn(results);
        return service;
    }

    private PostcodeService exceptionThrowingService() throws PostcodeServiceException {
        PostcodeService service = Mockito.mock(PostcodeService.class);
        when(service.lookup(any())).thenThrow(new PostcodeServiceException("", new RuntimeException("")));
        return service;
    }

    private PostcodeServiceResults greenpathResults() {
        PostcodeServiceResults results = new PostcodeServiceResults();
        results.setResults(Collections.singletonList(anyResult()));
        return results;
    }
    private PostcodeServiceResult anyResult() {
        PostcodeServiceResult result = new PostcodeServiceResult();
        result.setAddressLines(Collections.singletonList("address line 1"));
        result.setUprn("uprn");
        result.setPostcode("EH10 4AX");
        result.setTown("Edinburgh");
        return result;
    }

    private UriInfo uriInfoWithNoPostcodeParam() {
        URI uri = new ResteasyUriBuilder().build();
        return new ResteasyUriInfo(uri);
    }

    private UriInfo uriInfoWithPostcodeParam(String postcode) {
        URI uri = new ResteasyUriBuilder().queryParam("postcode", postcode).build();
        return new ResteasyUriInfo(uri);
    }

    private String scottishPostcode() {
        return "EH104AX";
    }

    private String normalisedScottishPostcode() {
        return "EH10 4AX";
    }

    private String englishPostcode() {
        return "B1 2HB";
    }

}
