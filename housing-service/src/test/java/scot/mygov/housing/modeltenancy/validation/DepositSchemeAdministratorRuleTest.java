package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertTrue(resultsBuilder.build().getIssues().isEmpty());
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
        assertFalse(resultsBuilder.build().getIssues().isEmpty());
    }
}
