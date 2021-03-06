package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DepositSchemeAdministratorRuleTest {

    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();
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
    public void accceptsEmptyAdmin() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setTenancyDepositSchemeAdministrator("");

        // ACT
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();
        sut.validate(modelTenancy, resultsBuilder);


        // ASSERT
        assertTrue(resultsBuilder.build().getIssues().isEmpty());
    }

    @Test
    public void accceptsNullAdmin() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setTenancyDepositSchemeAdministrator(null);

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
