package scot.mygov.housing.postcode.mapcloud;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.postcode.PostcodeService;
import scot.mygov.housing.postcode.PostcodeServiceException;
import scot.mygov.housing.postcode.PostcodeServiceResult;
import scot.mygov.housing.postcode.PostcodeServiceResults;
import scot.mygov.housing.rpz.PostcodeSource;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapcloudPostcodeServiceTest {

    @Test
    public void invalidPostcodeReturnsExpectedResults() throws PostcodeServiceException {

        // ARRANGE
        PostcodeService sut = new MapcloudPostcodeService(postcodeSource(), anyTarget(), "anyUser", "anyPassword");

        // ACT
        PostcodeServiceResults actual = sut.lookup("invalid");

        // ASSERT
        Assert.assertFalse(actual.isValidPostcode());
        Assert.assertFalse(actual.isScottishPostcode());
        Assert.assertTrue(actual.getResults().isEmpty());
    }

    @Test
    public void englishPostcodeReturnsExpectedResults() throws PostcodeServiceException {

        // ARRANGE
        PostcodeService sut = new MapcloudPostcodeService(postcodeSource(), anyTarget(), "anyUser", "anyPassword");

        // ACT
        PostcodeServiceResults actual = sut.lookup(englishPostcode());

        // ASSERT
        Assert.assertTrue(actual.isValidPostcode());
        Assert.assertFalse(actual.isScottishPostcode());
        Assert.assertTrue(actual.getResults().isEmpty());
    }

    @Test
    public void scottishPostcodeReturnsExpectedResults() throws PostcodeServiceException {

        // ARRANGE
        MapcloudPostcodeResults results = greenpathMapcloudResults();
        PostcodeService sut = new MapcloudPostcodeService(postcodeSource(), greenpathTarget(results), "anyUser", "anyPassword");

        // ACT
        PostcodeServiceResults actual = sut.lookup(scottishPostcode());

        // ASSERT
        Assert.assertTrue(actual.isValidPostcode());
        Assert.assertTrue(actual.isScottishPostcode());
        Assert.assertFalse(actual.getResults().isEmpty());

        PostcodeServiceResult result1 = actual.getResults().get(0);
        Assert.assertEquals(result1.getPostcode(), results.getResults().get(0).getPostcode(), scottishPostcode());
        List<String> expectedAddressLines = new ArrayList<>();
        Collections.addAll(expectedAddressLines,
                results.getResults().get(0).getAddressLine1(),
                results.getResults().get(0).getAddressLine2(),
                results.getResults().get(0).getAddressLine3());
        Assert.assertEquals(expectedAddressLines, result1.getAddressLines());
        Assert.assertEquals(results.getResults().get(0).getUprn(), result1.getUprn());
        Assert.assertEquals(results.getResults().get(0).getTown(), result1.getTown());
    }

    @Test
    public void emptyASddressLinesAreOmitted() throws PostcodeServiceException {

        // ARRANGE
        MapcloudPostcodeResults results = greenpathMapcloudResults();
        // addess line 2 shoudl be missing from the results
        results.getResults().get(0).setAddressLine2("");
        PostcodeService sut = new MapcloudPostcodeService(postcodeSource(), greenpathTarget(results), "anyUser", "anyPassword");

        // ACT
        PostcodeServiceResults actual = sut.lookup(scottishPostcode());

        // ASSERT
        Assert.assertTrue(actual.isValidPostcode());
        Assert.assertTrue(actual.isScottishPostcode());
        Assert.assertFalse(actual.getResults().isEmpty());

        PostcodeServiceResult result1 = actual.getResults().get(0);
        Assert.assertEquals(result1.getPostcode(), results.getResults().get(0).getPostcode(), scottishPostcode());
        List<String> expectedAddressLines = new ArrayList<>();
        Collections.addAll(expectedAddressLines,
                results.getResults().get(0).getAddressLine1(),
                results.getResults().get(0).getAddressLine3());
        Assert.assertEquals(expectedAddressLines, result1.getAddressLines());
        Assert.assertEquals(results.getResults().get(0).getUprn(), result1.getUprn());
        Assert.assertEquals(results.getResults().get(0).getTown(), result1.getTown());
    }


    @Test(expected = PostcodeServiceException.class)
    public void errorFromMapcloudTargetThrowsException() throws PostcodeServiceException {

        // ARRANGE
        MapcloudPostcodeResults results = greenpathMapcloudResults();
        PostcodeService sut = new MapcloudPostcodeService(postcodeSource(), serverErrorTarget(), "anyUser", "anyPassword");

        // ACT
        PostcodeServiceResults actual = sut.lookup(scottishPostcode());

        // ASSERT -- see expected exception
    }

    private PostcodeSource postcodeSource() {
        PostcodeSource source = Mockito.mock(PostcodeSource.class);

        // return null for the english postcode
        Mockito.when(source.postcode(Mockito.eq(englishPostcode()))).thenReturn(null);

        // return a scottosh postcode with normalised version
        Postcode scottishPostcodeObj = new Postcode();
        scottishPostcodeObj.setDistrict("distrcit");
        scottishPostcodeObj.setNormalisedPostcode(normalisedScottishPostcode());
        scottishPostcodeObj.setPostcode(scottishPostcode());
        Mockito.when(source.postcode(Mockito.eq(scottishPostcode()))).thenReturn(scottishPostcodeObj);
        return source;
    }

    private String englishPostcode() {
        return "B12HB";
    }

    private String scottishPostcode() {
        return "EH104AX";
    }

    private String normalisedScottishPostcode() {
        return "EH10 4AX";
    }

    private WebTarget anyTarget() {
        return mock(WebTarget.class);
    }

    private WebTarget greenpathTarget(MapcloudPostcodeResults results) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(any(), any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        when(builder.header(any(), any())).thenReturn(builder);
        Response response = Mockito.mock(Response.class);
        when(response.readEntity(any(Class.class))).thenReturn(results);
        when(response.getStatus()).thenReturn(200);
        when(builder.get()).thenReturn(response);
        return target;
    }

    private WebTarget serverErrorTarget() {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(any(), any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        when(builder.header(any(), any())).thenReturn(builder);
        Response response = Mockito.mock(Response.class);
        when(response.getEntity()).thenReturn("there was an error");
        when(response.getStatus()).thenReturn(500);
        when(builder.get()).thenReturn(response);
        return target;
    }

    private MapcloudPostcodeResults greenpathMapcloudResults() {
        MapcloudPostcodeResults res = new MapcloudPostcodeResults();
        List<MapcloudPostcodeResult> resultsList = new ArrayList<>();
        resultsList.add(anyResult());
        res.setResults(resultsList);
        return res;
    }

    private MapcloudPostcodeResult anyResult() {
        MapcloudPostcodeResult res = new MapcloudPostcodeResult();
        res.setPostcode(scottishPostcode());
        res.setUprn("1111");
        res.setAddressLine1("line1");
        res.setAddressLine2("line2");
        res.setAddressLine3("line3");
        res.setTown("town");
        return res;
    }
}
