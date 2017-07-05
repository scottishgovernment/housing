package scot.mygov.housing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HealthcheckTest {

    @Test
    public void okForGreeenpath() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(validLicense(), target(200));

        // ACT
        Response response = sut.health();

        // ASSERT
        ObjectNode health = (ObjectNode) response.getEntity();
        assertEquals("licence not as expected", true, health.get("license").asBoolean());
        assertEquals("geosearch not as expected", true, health.get("geosearch").asBoolean());
    }

    @Test
    public void notOkForNotLicensed() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(invalidLicense(), target(200));

        // ACT
        Response response = sut.health();

        // ASSERT
        ObjectNode health = (ObjectNode) response.getEntity();
        assertEquals("licence not as expected", false, health.get("license").asBoolean());
        assertEquals("geosearch not as expected", true, health.get("geosearch").asBoolean());
    }

    @Test
    public void notOkForExceptionFromGeo() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(validLicense(), exceptionTarget());

        // ACT
        Response response = sut.health();

        // ASSERT
        ObjectNode health = (ObjectNode) response.getEntity();
        assertEquals("licence not as expected", true, health.get("license").asBoolean());
        assertEquals("geosearch not as expected", false, health.get("geosearch").asBoolean());
    }

    @Test
    public void notOkForErrorResponseFromGeo() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(validLicense(), target(503));

        // ACT
        Response response = sut.health();

        // ASSERT
        ObjectNode health = (ObjectNode)response.getEntity();
        assertEquals("licence not as expected", true, health.get("license").asBoolean());
        assertEquals("geosearch not as expected", false, health.get("geosearch").asBoolean());
    }

    private AsposeLicense validLicense() {
        AsposeLicense license = Mockito.mock(AsposeLicense.class);
        when(license.isLicensed()).thenReturn(true);
        return license;
    }

    private AsposeLicense invalidLicense() {
        AsposeLicense license = Mockito.mock(AsposeLicense.class);
        when(license.isLicensed()).thenReturn(false);
        return license;
    }

    private WebTarget exceptionTarget() {
        WebTarget target = Mockito.mock(WebTarget.class);
        Invocation.Builder builder = Mockito.mock(Invocation.Builder.class);
        when(builder.get()).thenThrow(new ProcessingException("processing exception"));
        when(target.request()).thenReturn(builder);
        return target;
    }

    private WebTarget target(int status) {
        WebTarget target = Mockito.mock(WebTarget.class);
        Invocation.Builder builder = Mockito.mock(Invocation.Builder.class);
        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(status);
        when(builder.get()).thenReturn(response);
        when(target.request()).thenReturn(builder);
        return target;
    }
}
