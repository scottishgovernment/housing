package scot.mygov.housing.mapcloud;

import com.codahale.metrics.MetricRegistry;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MapcloudTest {

    @Test
    public void greenpathPostcode() throws MapcloudException {

        // ARRANGE
        MapcloudResults results = postcodeResults("eh104ax");
        Mapcloud sut = new Mapcloud(targetWithResults(results), "", "", new MetricRegistry());

        // ACT
        MapcloudResults actual = sut.lookupPostcode("uprn");

        // ASSERT
        assertEquals(actual.getResults().size(), results.getResults().size());
        assertEquals(actual.getResults().get(0).getPostcode(), "eh104ax");
    }

    @Test
    public void greenpathUprn() throws MapcloudException {

        // ARRANGE
        MapcloudResults results = uprnResults("anyuprn");
        Mapcloud sut = new Mapcloud(targetWithResults(results), "", "", new MetricRegistry());

        // ACT
        MapcloudResults actual = sut.lookupUprn("uprn");

        // ASSERT
        assertEquals(actual.getResults().size(), 1);
        assertEquals(actual.getResults().get(0).getUprn(), "anyuprn");

    }

    @Test(expected=MapcloudException.class)
    public void processingExceptionWrappedAsExpected() throws MapcloudException {
        // ARRANGE
        Mapcloud sut = new Mapcloud(
                exceptionThrowingTarget(new ProcessingException("")), "", "", new MetricRegistry());

        // ACT
        MapcloudResults actual = sut.lookupUprn("uprn");

        // ASSERT -- see expected exception
    }

    @Test(expected=MapcloudException.class)
    public void webApplicationExceptionWrappedAsExpected() throws MapcloudException {
        // ARRANGE
        Mapcloud sut = new Mapcloud(
                exceptionThrowingTarget(new ProcessingException("")), "", "", new MetricRegistry());

        // ACT
        MapcloudResults actual = sut.lookupUprn("uprn");

        // ASSERT -- see expected exception
    }

    private MapcloudResults uprnResults(String uprn) {
        MapcloudResults results = new MapcloudResults();
        results.setResults(singletonList(uprnResult(uprn)));
        results.setTotalResults(1);
        return results;
    }

    private MapcloudResults postcodeResults(String postcode) {
        MapcloudResults results = new MapcloudResults();
        results.setResults(singletonList(postcodeResult(postcode)));
        results.setTotalResults(1);
        return results;
    }

    private MapcloudResult uprnResult(String uprn) {
        MapcloudResult result = new MapcloudResult();
        result.setUprn(uprn);
        result.setPostcode("EH10 4AX");
        result.setAddressLine1("address line 1");
        result.setAddressLine2("address line 2");
        result.setAddressLine3("address line 3");
        result.setTown("Edinburgh");
        return result;
    }

    private MapcloudResult postcodeResult(String postcode) {
        MapcloudResult result = new MapcloudResult();
        result.setUprn(RandomStringUtils.random(6));
        result.setPostcode(postcode);
        result.setAddressLine1("address line 1");
        result.setAddressLine2("address line 2");
        result.setAddressLine3("address line 3");
        result.setTown("Edinburgh");
        return result;
    }

    private WebTarget targetWithResults(MapcloudResults results) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(eq("uprn"), any())).thenReturn(target);
        when(target.queryParam(eq("pc"), any())).thenReturn(target);
        when(target.queryParam(eq("addrformat"), eq(2))).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(200);
        when(builder.header(any(), any())).thenReturn(builder);
        when(builder.get(MapcloudResults.class)).thenReturn(results);
        return target;
    }

    private WebTarget exceptionThrowingTarget(Throwable t) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(eq("uprn"), any())).thenReturn(target);
        when(target.queryParam(eq("pc"), any())).thenReturn(target);
        when(target.queryParam(eq("addrformat"), eq(2))).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(200);
        when(builder.header(any(), any())).thenReturn(builder);
        when(builder.get(MapcloudResults.class)).thenThrow(t);
        return target;
    }
}
