package scot.mygov.housing.postcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import scot.mygov.housing.mapcloud.DPAMapcloudResult;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.LGGMapcloudResult;
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

        // the results should have been sorted correctly
        assertEquals(actual.getResults().get(0).getStreet(), results.getResults().get(3).getAddressStreet());
        assertEquals(actual.getResults().get(1).getStreet(), results.getResults().get(2).getAddressStreet());
        assertEquals(actual.getResults().get(2).getStreet(), results.getResults().get(1).getAddressStreet());
        assertEquals(actual.getResults().get(3).getStreet(), results.getResults().get(0).getAddressStreet());
    }

    private String scottishPostcode() {
        return "EH10 4AX";
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
        List<DPAMapcloudResult> resultsList = new ArrayList<>();
        DPAMapcloudResult one = anyResult();
        DPAMapcloudResult two = anyResult();
        DPAMapcloudResult three = anyResult();
        DPAMapcloudResult four = anyResult();
        one.setAddressStreet("111 Some street");
        two.setAddressStreet("11 Some street");
        three.setAddressStreet("1 Some street");
        four.setAddressStreet("Some street");
        resultsList.add(one);
        resultsList.add(two);
        resultsList.add(three);
        resultsList.add(four);
        res.setResults(resultsList);
        return res;
    }

    private DPAMapcloudResult anyResult() {
        DPAMapcloudResult result = new DPAMapcloudResult();
        result.setUprn(RandomStringUtils.randomAlphabetic(6));
        result.setPostcode("EH10 4AX");
        result.setAddressBuilding("address building");
        result.setAddressStreet("address street");
        result.setAddressLocality("address locality");
        result.setPostTown("Edinburgh");
        return result;
    }

}
