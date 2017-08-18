package scot.mygov.housing.postcode;

import org.junit.Test;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.MapcloudResult;
import scot.mygov.housing.mapcloud.MapcloudResults;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class MapcloudPostcodeServiceTest {

    @Test(expected = PostcodeServiceException.class)
    public void mapcloudExceptionHandledCorrectly() throws PostcodeServiceException, MapcloudException {

        // ARRANGE
        PostcodeService sut = new MapcloudPostcodeService(excpetionThrowingMapcloud());

        // ACT
        sut.lookup(scottishPostcode());

        // ASSERT -- see expected exception
    }

    @Test
    public void greenpathResultsParsedCorrectly() throws PostcodeServiceException, MapcloudException {

        // ARRANGE
        MapcloudResults results = greenpathMapcloudResults();
        PostcodeService sut = new MapcloudPostcodeService(mapcloudWithResults(results));

        // ACT
        PostcodeServiceResults actual = sut.lookup(scottishPostcode());

        // ASSERT
        assertFalse(actual.getResults().isEmpty());

        PostcodeServiceResult result1 = actual.getResults().get(0);
        assertEquals(result1.getPostcode(), results.getResults().get(0).getPostcode(), scottishPostcode());

        List<String> expectedAddressLines = new ArrayList<>();
        addAll(expectedAddressLines,
                results.getResults().get(0).getAddressLine1(),
                results.getResults().get(0).getAddressLine2(),
                results.getResults().get(0).getAddressLine3());
        assertEquals(expectedAddressLines, result1.getAddressLines());
        assertEquals(results.getResults().get(0).getUprn(), result1.getUprn());
        assertEquals(results.getResults().get(0).getTown(), result1.getTown());
    }

    @Test
    public void emptyAddressLinesAreOmitted() throws PostcodeServiceException, MapcloudException {

        // ARRANGE
        MapcloudResults results = greenpathMapcloudResults();
        // address line 2 should be missing from the results
        results.getResults().get(0).setAddressLine2("");
        PostcodeService sut = new MapcloudPostcodeService(mapcloudWithResults(results));

        // ACT
        PostcodeServiceResults actual = sut.lookup(scottishPostcode());

        // ASSERT
        assertFalse(actual.getResults().isEmpty());

        PostcodeServiceResult result1 = actual.getResults().get(0);
        assertEquals(result1.getPostcode(), results.getResults().get(0).getPostcode(), scottishPostcode());

        List<String> expectedAddressLines = new ArrayList<>();
        addAll(expectedAddressLines,
                results.getResults().get(0).getAddressLine1(),
                results.getResults().get(0).getAddressLine3());
        assertEquals(expectedAddressLines, result1.getAddressLines());
        assertEquals(results.getResults().get(0).getUprn(), result1.getUprn());
        assertEquals(results.getResults().get(0).getTown(), result1.getTown());
    }


    private String scottishPostcode() {
        return "EH104AX";
    }

    private Mapcloud excpetionThrowingMapcloud() throws MapcloudException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupPostcode(any())).thenThrow(new MapcloudException("arg", new RuntimeException("arg")));
        return mapcloud;
    }

    private Mapcloud mapcloudWithResults(MapcloudResults results) throws MapcloudException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupPostcode(any())).thenReturn(results);
        return mapcloud;
    }


    private WebTarget greenpathTarget(MapcloudResults results) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(any(), any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        when(builder.header(any(), any())).thenReturn(builder);
        when(builder.get(eq(MapcloudResults.class))).thenReturn(results);
        return target;
    }

    private WebTarget serverErrorTarget(RuntimeException ex) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(any(), any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        when(builder.header(any(), any())).thenReturn(builder);
        when(builder.get(eq(MapcloudResults.class))).thenThrow(ex);
        return target;
    }

    private MapcloudResults greenpathMapcloudResults() {
        MapcloudResults res = new MapcloudResults();
        List<MapcloudResult> resultsList = new ArrayList<>();
        resultsList.add(anyResult());
        res.setResults(resultsList);
        return res;
    }

    private MapcloudResult anyResult() {
        MapcloudResult res = new MapcloudResult();
        res.setPostcode(scottishPostcode());
        res.setUprn("1111");
        res.setAddressLine1("line1");
        res.setAddressLine2("line2");
        res.setAddressLine3("line3");
        res.setTown("town");
        return res;
    }

}
