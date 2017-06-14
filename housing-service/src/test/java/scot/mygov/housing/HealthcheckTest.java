package scot.mygov.housing;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.rpz.PostcodeSource;

import javax.ws.rs.core.Response;

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
        Assert.assertEquals(Healthcheck.HealthResult.class, response.getEntity().getClass());
        Assert.assertEquals(response.getStatus(), 200);
        Healthcheck.HealthResult res = (Healthcheck.HealthResult) response.getEntity();
        Assert.assertTrue(res.isOk());
        Assert.assertEquals(res.getMessage(), "ok");
    }

    @Test
    public void serverErrorForUnexpectedPostcodeSource() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(goodPostCodeSource("unexpected"));

        // ACT
        Response response = sut.health();

        // ASSERT
        Assert.assertEquals(Healthcheck.HealthResult.class, response.getEntity().getClass());
        Assert.assertEquals(response.getStatus(), 500);
        Healthcheck.HealthResult res = (Healthcheck.HealthResult) response.getEntity();
        Assert.assertFalse(res.isOk());
        Assert.assertNotNull(res.getMessage());
    }

    @Test
    public void serverErrorForBadPostcodeSource() {

        // ARRANGE
        Healthcheck sut = new Healthcheck(badPostCodeSource());

        // ACT
        Response response = sut.health();

        // ASSERT
        Assert.assertEquals(Healthcheck.HealthResult.class, response.getEntity().getClass());
        Assert.assertEquals(response.getStatus(), 500);
        Healthcheck.HealthResult res = (Healthcheck.HealthResult) response.getEntity();
        Assert.assertFalse(res.isOk());
        Assert.assertNotNull(res.getMessage());
    }

    PostcodeSource badPostCodeSource() {
        PostcodeSource postcodeSource = Mockito.mock(PostcodeSource.class);
        Mockito.when(postcodeSource.postcode(ArgumentMatchers.any())).thenThrow(new RuntimeException("arg"));
        return postcodeSource;
    }

    PostcodeSource goodPostCodeSource(String postcodeIn) {
        PostcodeSource postcodeSource = Mockito.mock(PostcodeSource.class);
        Postcode postcode = new Postcode();
        postcode.setPostcode(postcodeIn);
        postcode.setDistrict("district");
        Mockito.when(postcodeSource.postcode(ArgumentMatchers.any())).thenReturn(postcode);
        return postcodeSource;
    }
}
