package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertEquals;

public class LandlordsRuleTest {

    private final ModelTenancyObjectMother om = new ModelTenancyObjectMother();
    private final LandlordsRule rule = new LandlordsRule();

    @Test
    public void rejectZeroLandlords() {
        // ARRANGE
        ModelTenancy input = om.tenancyWithNoLandlords();
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(1, results.getIssues().size());
    }

    @Test
    public void acceptsOneValidLandlord() {
        // ARRANGE
        ModelTenancy input = om.tenancyWithOneLandlord();
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(0, results.getIssues().size());
    }

    @Test
    public void acceptsTwoValidLandlords() {
        // ARRANGE
        ModelTenancy input = om.tenancyWithTwoLandlords();
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(0, results.getIssues().size());
    }

    @Test
    public void rejectsMissingName() {
        // ARRANGE
        ModelTenancy input = om.tenancyWithTwoLandlords();
        ValidationResultsBuilder builder = new ValidationResultsBuilder();
        input.getLandlords().get(0).setName(null);

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(1, results.getIssues().size());
    }

    @Test
    public void rejectsInvalidEmail() {
        // ARRANGE
        ModelTenancy input = om.tenancyWithTwoLandlords();
        ValidationResultsBuilder builder = new ValidationResultsBuilder();
        input.getLandlords().get(0).setEmail("aaa");

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(1, results.getIssues().size());
    }

    @Test
    public void rejectsNoContactInfo() {
        // ARRANGE
        ModelTenancy input = om.tenancyWithTwoLandlords();
        ValidationResultsBuilder builder = new ValidationResultsBuilder();
        input.getLandlords().get(0).setEmail(null);
        input.getLandlords().get(0).setTelephone(null);

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(1, results.getIssues().size());
    }


}
