package scot.mygov.housing.postcode;

import javafx.geometry.Pos;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.mapcloud.MapcloudResults;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeoPostcodeSourceTest {

    @Test
    public void greenpath() throws PostcodeServiceException {
        // ARRANGE
        GeoPostcodeSource sut = new GeoPostcodeSource(targetWithPostCode(validPostcodeObj()));

        // ACT
        Postcode actual = sut.postcode(validPostcode());

        // ASSERT
        assertEquals(actual.getPostcode(), validPostcode());
    }

    @Test
    public void notFoundReturnsNull() throws PostcodeServiceException {
        // ARRANGE
        GeoPostcodeSource sut = new GeoPostcodeSource(targetNotFound());

        // ACT
        Postcode actual = sut.postcode(englishPostcode());

        // ASSERT
        assertNull(actual);
    }

    @Test(expected = PostcodeServiceException.class)
    public void exceptionWrappedeAsExpected() throws PostcodeServiceException {
        // ARRANGE
        GeoPostcodeSource sut = new GeoPostcodeSource(exceptionThrowingTarget());

        // ACT
        Postcode actual = sut.postcode(englishPostcode());

        // ASSERT -- see expected
    }

    @Test(expected = PostcodeServiceException.class)
    public void serverErrorThrowsException() throws PostcodeServiceException {
        // ARRANGE
        GeoPostcodeSource sut = new GeoPostcodeSource(serverErrorTarget());

        // ACT
        Postcode actual = sut.postcode(englishPostcode());

        // ASSERT -- see expected
    }

    private String validPostcode() {
        return "EH104AX";
    }

    private String englishPostcode() {
        return "B1 2HB";
    }

    private Postcode validPostcodeObj() {
        Postcode postcode = new Postcode();
        postcode.setPostcode("EH104AX");
        postcode.setDistrict("");
        postcode.setNormalisedPostcode("EH10 4AX");
        return postcode;
    }

    private WebTarget targetWithPostCode(Postcode postcode) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(200);
        when(response.readEntity(Postcode.class)).thenReturn(postcode);
        when(builder.get()).thenReturn(response);
        return target;
    }

    private WebTarget targetNotFound() {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(404);
        when(builder.get()).thenReturn(response);
        return target;
    }

    private WebTarget exceptionThrowingTarget() {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(404);
        when(builder.get()).thenThrow(new ProcessingException("processing problem"));
        return target;
    }

    private WebTarget serverErrorTarget() {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getEntity()).thenReturn("server error");
        when(response.getStatus()).thenReturn(500);
        when(builder.get()).thenReturn(response);
        return target;
    }
}
