package scot.mygov.housing;

import org.jboss.resteasy.specimpl.ResteasyUriBuilder;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.rpz.RPZ;
import scot.mygov.housing.rpz.RPZResult;
import scot.mygov.housing.rpz.RPZService;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;

/**
 * Created by z418868 on 14/06/2017.
 */
public class HousingResourceTest {


    // missing postcode
    @Test
    public void missingPostCodeReturnsError() {

        // ARRANGE
        HousingResource sut = new HousingResource(null);
        MultivaluedMap<String, String> params = null;
        URI uri = new ResteasyUriBuilder().queryParam("date", "2010-01-01").build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        Assert.assertEquals(HousingResource.ValidationResult.class, response.getEntity().getClass());
        Assert.assertEquals(response.getStatus(), 400);
        HousingResource.ValidationResult validationResult = (HousingResource.ValidationResult) response.getEntity();
        Assert.assertTrue(validationResult.getErrors().containsKey("postcode"));
    }


    // missing date
    @Test
    public void missingDateReturnsError() {

        // ARRANGE
        HousingResource sut = new HousingResource(null);
        MultivaluedMap<String, String> params = null;
        URI uri = new ResteasyUriBuilder().queryParam("postcode", "EH104AX").build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        Assert.assertEquals(HousingResource.ValidationResult.class, response.getEntity().getClass());
        Assert.assertEquals(response.getStatus(), 400);
        HousingResource.ValidationResult validationResult = (HousingResource.ValidationResult) response.getEntity();
        Assert.assertTrue(validationResult.getErrors().containsKey("date"));
    }

    // invalid date
    @Test
    public void invalidDateReturnsError() {

        // ARRANGE
        HousingResource sut = new HousingResource(null);
        MultivaluedMap<String, String> params = null;
        URI uri = new ResteasyUriBuilder()
                .queryParam("postcode", "EH104AX")
                .queryParam("date", "111")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        Assert.assertEquals(HousingResource.ValidationResult.class, response.getEntity().getClass());
        Assert.assertEquals(response.getStatus(), 400);
        HousingResource.ValidationResult validationResult = (HousingResource.ValidationResult) response.getEntity();
        Assert.assertTrue(validationResult.getErrors().containsKey("date"));
    }

    // returns result from service
    @Test
    public void returnsResultFromService() {

        // ARRANGE
        RPZ rpz = new RPZ("title", LocalDate.MIN, LocalDate.MAX, 100, Collections.emptySet());
        RPZResult result = new RPZResult.Builder().rpz(rpz).build();
        RPZService service = rpzService(result);
        HousingResource sut = new HousingResource(service);
        MultivaluedMap<String, String> params = null;
        URI uri = new ResteasyUriBuilder()
                .queryParam("postcode", "EH104AX")
                .queryParam("date", "2012-10-10")
                .build();
        UriInfo uriInfo = new ResteasyUriInfo(uri);

        // ACT
        Response response = sut.rpz(uriInfo);

        // ASSERT
        Assert.assertEquals(RPZResult.class, response.getEntity().getClass());
        Assert.assertEquals(response.getStatus(), 200);
        RPZResult rpzResult = (RPZResult) response.getEntity();
        Assert.assertTrue(rpzResult.isValidPostcode());
        Assert.assertTrue(rpzResult.isScottishPostcode());
        Assert.assertTrue(rpzResult.isInRentPressureZone());
        Assert.assertEquals(rpzResult.getRentPressureZoneTitle(), rpz.getTitle());
        Assert.assertEquals(rpzResult.getMaxIncrease(), rpz.getMaxRentIncrease(), 0);
    }

    public RPZService rpzService(RPZResult result) {
        return new RPZService() {
            public RPZResult rpz(String postcode, LocalDate date) {
                return result;
            }
        };
    }
}
