package scot.mygov.housing.forms.rentincreasenotice;

import org.junit.Test;
import scot.mygov.housing.forms.rentincreasenotice.model.RentIncrease;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertEquals("_", output.get("inRentPressureZoneCheckbox"));
        assertEquals("X", output.get("notInRentPressureZoneCheckbox"));
    }

    @Test
    public void inRPZ() {

        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setInRentPressureZone("true");

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals("X", output.get("inRentPressureZoneCheckbox"));
        assertEquals("_", output.get("notInRentPressureZoneCheckbox"));
    }

    @Test
    public void nullInRPZ() {

        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setInRentPressureZone(null);

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals("_", output.get("inRentPressureZoneCheckbox"));
        assertEquals("_", output.get("notInRentPressureZoneCheckbox"));
    }

    @Test
    public void rentCapSenetenceEmptyWithoutStartDate() {
        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setCapFromDate(null);
        input.setCapToDate(LocalDate.now());

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals("", output.get("capToAndFromSentence"));
    }

    @Test
    public void rentCapSenetenceEmptyWithoutEndDate() {
        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setCapFromDate(LocalDate.now());
        input.setCapToDate(null);

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals("", output.get("capToAndFromSentence"));
    }

    @Test
    public void rentCapExtractedWithBothDates() {
        // ARRANGE
        RentIncrease input = new RentIncrease();
        input.setCapFromDate(LocalDate.now());
        input.setCapToDate(LocalDate.now());

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertNotNull(output.get("capToAndFromSentence"));
    }
}
