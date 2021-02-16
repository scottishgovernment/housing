package scot.mygov.housing.forms.modeltenancy.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by z418868 on 16/02/2021.
 */
public class FurnishingTypeTest {

    @Test
    public void returnsEmptyStringForNull() {
        assertEquals("", FurnishingType.describe(null));
    }

    @Test
    public void returnsEmptyStringForEmptyValue() {
        assertEquals("", FurnishingType.describe(""));
    }

    @Test
    public void describesValidValue() {
        assertEquals("unfurnished", FurnishingType.describe("UNFURNISHED"));
    }

    @Test
    public void returnsInputStringForInvalidVale() {
        assertEquals("INVALID", FurnishingType.describe("INVALID"));
    }
}
