package scot.mygov.housing.forms.rentadjudication;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.FieldExtractorUtils;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scot.mygov.housing.forms.FieldExtractorUtils.NOT_APPLICABLE;

public class RentAdjudicationFieldExtractorTest {

    RentAdjudicationFieldExtractor sut = new RentAdjudicationFieldExtractor();

    @Test
    public void canExtractFields() {

        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();

        Set<String> expectedFieldNames = new HashSet<>();
        Collections.addAll(expectedFieldNames,
                "currentRentAmount", "currentRentFrequency",
                "damages", "doubleGlazing", "furnished",
                "hasDamages", "hasIncluded", "hasLandlordImprovements", "hasServices", "hasSharedAreas",
                "hasTenantImprovements",
                "heating", "included", "landlordImprovements", "landlords", "landlordsAgent",
                "newRentAmount", "newRentFrequency",
                "propertyType", "rooms",
                "servicesCostDetails", "servicesDetails", "sharedAreas",
                "tenantImprovements", "tenants", "tenantsAgent"
            );

        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT - we are just testing that the expected fields were extracted, exact behaviour would be better tested in individual methods.
        assertTrue(fields.keySet().containsAll(expectedFieldNames));
    }

    @Test
    public void sharedAreasEmptyExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setSharedAreas("");
        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasSharedAreas"), "No");
        assertEquals(fields.get("sharedAreas"), NOT_APPLICABLE);
    }

    @Test
    public void sharedAreasNullExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setSharedAreas(null);
        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasSharedAreas"), "No");
        assertEquals(fields.get("sharedAreas"), NOT_APPLICABLE);
    }

    @Test
    public void includedNullExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setIncluded(null);
        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasIncluded"), "No");
        assertEquals(fields.get("included"), NOT_APPLICABLE);
    }

    @Test
    public void includedEmptyExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setIncluded(null);
        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasIncluded"), "No");
        assertEquals(fields.get("included"), NOT_APPLICABLE);
    }

    @Test
    public void servicesNullExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setServicesDetails(null);
        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasServices"), "No");
    }

    @Test
    public void servicesNullEmptyAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setServicesDetails(null);
        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasServices"), "No");
    }

    @Test
    public void tenantImprovementsNullExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setImprovementsTenant(null);

        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasTenantImprovements"), "No");
    }

    @Test
    public void tenantImprovementsEmptyExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setImprovementsTenant("");

        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasTenantImprovements"), "No");
    }

    @Test
    public void landlordImprovementsNullExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setImprovementsLandlord(null);

        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasLandlordImprovements"), "No");
    }

    @Test
    public void landlordImprovementsEmptyExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setImprovementsLandlord("");

        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasLandlordImprovements"), "No");
    }

    @Test
    public void damagesNullExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setDamage(null);

        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasDamages"), "No");
    }

    @Test
    public void damagesEmptyExtractedAsExpected() {
        // ARRANGE
        RentAdjudication input = RentAdjudicationObjectMother.anyRentAdjudication();
        input.setDamage("");

        // ACT
        Map<String, Object> fields = sut.extractFields(input);

        // ASSERT
        assertEquals(fields.get("hasDamages"), "No");
    }
}
