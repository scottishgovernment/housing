package scot.mygov.housing.rpz;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import scot.mygov.geosearch.api.models.Postcode;

import java.time.LocalDate;
import java.util.Collections;

/**
 * Created by z418868 on 14/06/2017.
 */
public class InMemoryRPZServiceTest {

    @Test
    public void invalidPostcode() {

        // ARRANGE
        RPZService sut = new InMemoryRPZService(Collections.emptySet(), new PostcodeSource(null));

        // ACT
        RPZResult actual = sut.rpz("invalid", LocalDate.now());

        // ASSERT
        Assert.assertEquals(actual.isValidPostcode(), false);
        Assert.assertEquals(actual.isScottishPostcode(), false);
        Assert.assertEquals(actual.isInRentPressureZone(), false);
    }

    @Test
    public void nonScottishPostcode() {

        // ARRANGE
        RPZService sut = new InMemoryRPZService(Collections.emptySet(), postcodeSource());

        // ACT
        RPZResult actual = sut.rpz("nonScottish", LocalDate.now());

        // ASSERT
        Assert.assertEquals(actual.isValidPostcode(), true);
        Assert.assertEquals(actual.isScottishPostcode(), false);
        Assert.assertEquals(actual.isInRentPressureZone(), false);
    }

    @Test
    public void scottishPostcodeNotInRPZ() {

        // ARRANGE
        RPZService sut = new InMemoryRPZService(Collections.emptySet(), postcodeSource());

        // ACT
        RPZResult actual = sut.rpz("validScottishPostcode", LocalDate.now());

        // ASSERT
        Assert.assertEquals(actual.isValidPostcode(), true);
        Assert.assertEquals(actual.isScottishPostcode(), true);
        Assert.assertEquals(actual.isInRentPressureZone(), false);
    }


    @Test
    public void scottishPostcodeInRPZ() {

        // ARRANGE
        RPZ rpz = new RPZ("title", LocalDate.MIN, LocalDate.MAX, 10, Collections.singleton("validScottishPostcode"));
        RPZService sut = new InMemoryRPZService(Collections.singleton(rpz), postcodeSource());

        // ACT
        RPZResult actual = sut.rpz("validScottishPostcode", LocalDate.now());

        // ASSERT
        Assert.assertEquals(actual.isValidPostcode(), true);
        Assert.assertEquals(actual.isScottishPostcode(), true);
        Assert.assertEquals(actual.isInRentPressureZone(), true);
        Assert.assertEquals(actual.getMaxIncrease(), 10, 0);
        Assert.assertEquals(actual.getRentPressureZoneTitle(), "title");
    }

    @Test
    public void scottishPostcodeInRPZButOutsideDateRange() {

        // ARRANGE
        RPZ rpz = new RPZ("title", LocalDate.MIN, LocalDate.MIN, 10, Collections.singleton("validScottishPostcode"));
        RPZService sut = new InMemoryRPZService(Collections.singleton(rpz), postcodeSource());

        // ACT
        RPZResult actual = sut.rpz("validScottishPostcode", LocalDate.now());

        // ASSERT
        Assert.assertEquals(actual.isValidPostcode(), true);
        Assert.assertEquals(actual.isScottishPostcode(), true);
        Assert.assertEquals(actual.isInRentPressureZone(), false);
    }

    private PostcodeSource postcodeSource() {
        PostcodeSource postcodeSource = Mockito.mock(PostcodeSource.class);
        Mockito.when(postcodeSource.postcode(ArgumentMatchers.eq("validScottishPostcode"))).thenReturn(validPostcode());
        Mockito.when(postcodeSource.validPostcode(ArgumentMatchers.any())).thenReturn(true);
        return postcodeSource;
    }

    private Postcode validPostcode() {
        Postcode postocde = new Postcode();
        postocde.setPostcode("validScottishPostcode");
        postocde.setDistrict("district");
        return postocde;
    }
}
