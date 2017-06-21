package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

/**
 * Created by z418868 on 16/06/2017.
 */
public class LandlordsRuleTest {

    private final ObjectMother om = new ObjectMother();
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
        Assert.assertEquals(results.getIssues().size(), 1);
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
        Assert.assertEquals(results.getIssues().size(), 0);
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
        Assert.assertEquals(results.getIssues().size(), 0);
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
        Assert.assertEquals(results.getIssues().size(), 1);
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
        Assert.assertEquals(results.getIssues().size(), 1);
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
        Assert.assertEquals(results.getIssues().size(), 1);
    }


}
