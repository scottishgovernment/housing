package scot.mygov.housing.forms.rentincreasenotice;

import org.junit.Test;
import scot.mygov.housing.forms.rentincreasenotice.model.RentIncrease;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RentIncreaseFieldExtractorTest {

    RentIncreaseFieldExtractor sut = new RentIncreaseFieldExtractor();

    @Test
    public void notInRPZ() {

        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setInRentPressureZone("false");

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("inRentPressureZoneCheckbox"), "_");
        assertEquals(output.get("notInRentPressureZoneCheckbox"), "X");
    }

    @Test
    public void inRPZ() {

        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setInRentPressureZone("true");

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("inRentPressureZoneCheckbox"), "X");
        assertEquals(output.get("notInRentPressureZoneCheckbox"), "_");
    }

    @Test
    public void nullInRPZ() {

        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setInRentPressureZone(null);

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("inRentPressureZoneCheckbox"), "_");
        assertEquals(output.get("notInRentPressureZoneCheckbox"), "_");
    }
}
