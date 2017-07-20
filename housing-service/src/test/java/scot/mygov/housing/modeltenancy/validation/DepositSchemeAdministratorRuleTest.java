package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

public class DepositSchemeAdministratorRuleTest {

    private ObjectMother om = new ObjectMother();
    private DepositSchemeAdministratorRule sut = new DepositSchemeAdministratorRule();

    @Test
    public void accceptsRecognisedAdmin() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();

        // ACT
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();
        sut.validate(modelTenancy, resultsBuilder);


        // ASSERT
        Assert.assertTrue(resultsBuilder.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsUnrecognisedAdmin() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setTenancyDepositSchemeAdministrator("blah");
        // ACT
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();
        sut.validate(modelTenancy, resultsBuilder);


        // ASSERT
        Assert.assertFalse(resultsBuilder.build().getIssues().isEmpty());
    }
}
