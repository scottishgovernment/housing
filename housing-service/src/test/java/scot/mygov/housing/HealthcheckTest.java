package scot.mygov.housing;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.rpz.PostcodeSource;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by z418868 on 14/06/2017.
 */
public class HealthcheckTest {


    @Test
    public void okForGoodPostcodeSource() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(goodPostCodeSource(Healthcheck.KNOWN_POSTCODE));

        // ACT
        Response response = sut.health();

        // ASSERT
        assertEquals(Healthcheck.HealthResult.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 200);
        Healthcheck.HealthResult res = (Healthcheck.HealthResult) response.getEntity();
        Assert.assertTrue(res.isOk());
        assertEquals(res.getMessage(), "ok");
    }

    @Test
    public void serverErrorForUnexpectedPostcodeSource() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(goodPostCodeSource("unexpected"));

        // ACT
        Response response = sut.health();

        // ASSERT
        assertEquals(Healthcheck.HealthResult.class, response.getEntity().getClass());
        assertEquals(response.getStatus(), 500);
        Healthcheck.HealthResult res = (Healthcheck.HealthResult) response.getEntity();
        assertFalse(res.isOk());
        assertNotNull(res.getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void serverErrorForBadPostcodeSource() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(badPostCodeSource());

        // ACT
        Response response = sut.health();

        // ASSERT -- see expected exception
    }

    PostcodeSource badPostCodeSource() {
        PostcodeSource postcodeSource = Mockito.mock(PostcodeSource.class);
        when(postcodeSource.postcode(any())).thenThrow(new RuntimeException("arg"));
        return postcodeSource;
    }

    PostcodeSource goodPostCodeSource(String postcodeIn) {
        PostcodeSource postcodeSource = Mockito.mock(PostcodeSource.class);
        Postcode postcode = new Postcode();
        postcode.setPostcode(postcodeIn);
        postcode.setDistrict("district");
        when(postcodeSource.postcode(any())).thenReturn(postcode);
        return postcodeSource;
    }
}
