package scot.mygov.housing.postcode;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PostcodeServiceResultTest {

    @Test
    public void getStreetNameReturnEmptyStringForNull() {
        assertEquals(resWithStreet(null).getStreetName(), "");
    }

    @Test
    public void getStreetNameReturnEmptyStringForEmptyString() {
        assertEquals(resWithStreet("").getStreetName(), "");
    }

    @Test
    public void getStreetNameReturnWholeStreetIfNoSpace() {
        assertEquals(resWithStreet("NoSpacesInThisString").getStreetName(), "NoSpacesInThisString");
    }

    @Test
    public void getStreetNameReturnStreetAfterFirstSpace() {
        assertEquals(resWithStreet("111 This has some spaces").getStreetName(), "This has some spaces");
    }

    @Test
    public void getHouseNumberReturn0ForNull() {
        assertEquals(resWithStreet(null).getHouseNumber(), 0);
    }

    @Test
    public void getHouseNumberReturn0ForEmptyString() {
        assertEquals(resWithStreet("").getHouseNumber(), 0);
    }

    @Test
    public void getHouseNumberReturns0StringIfNoNumberAtStart() {
        assertEquals(resWithStreet("NoNumbersAtTheStartInThisString").getHouseNumber(), 0);
    }

    @Test
    public void blah() {
        assertEquals(resWithStreet("NoNumbers At The Start In This String").getHouseNumber(), 0);
    }

    @Test
    public void getStreetNumberReturnStreetAfterFirstSpace() {
        assertEquals(resWithStreet("111 This has some spaces").getHouseNumber(), 111);
    }

    private PostcodeServiceResult resWithStreet(String street) {
        PostcodeServiceResult res = new PostcodeServiceResult();
        res.setStreet(street);
        return res;
    }
}
