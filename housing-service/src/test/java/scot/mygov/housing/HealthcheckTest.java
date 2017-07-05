package scot.mygov.housing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HealthcheckTest {

    @Test
    public void okForGreeenpath() {

        // ARRANGE
        LocalDate now = LocalDate.now();
        Healthcheck sut = new Healthcheck(validLicense(now, 100L), target(200));

        // ACT
        Response response = sut.health();

        // ASSERT
        ObjectNode health = (ObjectNode) response.getEntity();
        assertEquals("licence not as expected", true, health.get("license").asBoolean());
        assertEquals("geosearch not as expected", true, health.get("geosearch").asBoolean());
        assertEquals("days until expiry not as expected", 100L, health.get("daysUntilExpiry").asLong());
        assertEquals("licenseExpires not as expected", now.toString(), health.get("licenseExpires").asText());
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
        Healthcheck sut = new Healthcheck(anyValidLicense(), exceptionTarget());

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
        Healthcheck sut = new Healthcheck(anyValidLicense(), target(503));

        // ACT
        Response response = sut.health();

        // ASSERT
        ObjectNode health = (ObjectNode)response.getEntity();
        assertEquals("licence not as expected", true, health.get("license").asBoolean());
        assertEquals("geosearch not as expected", false, health.get("geosearch").asBoolean());
    }

    private AsposeLicense anyValidLicense() {
        AsposeLicense license = Mockito.mock(AsposeLicense.class);
        when(license.isLicensed()).thenReturn(true);
        when(license.daysUntilExpiry()).thenReturn(100L);
        when(license.expires()).thenReturn(LocalDate.now());
        return license;
    }


    private AsposeLicense validLicense(LocalDate expires, long daysUntilExpiry) {
        AsposeLicense license = Mockito.mock(AsposeLicense.class);
        when(license.isLicensed()).thenReturn(true);
        when(license.daysUntilExpiry()).thenReturn(daysUntilExpiry);
        when(license.expires()).thenReturn(expires);

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
