package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertEquals;

public class CommunicationsAgreementRuleTest {

    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();
    private CommunicationsAgreementRule rule = new CommunicationsAgreementRule();

    @Test
    public void acceptsNoEmailsForHardCopyAgreement() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.getTenants().stream().forEach(tenant -> tenant.setEmail(""));
        modelTenancy.setCommunicationsAgreement(CommunicationsAgreement.HARDCOPY.name());
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, builder);

        // ASSERT
        assertEquals(builder.build().getIssues().size(), 0);
    }

    @Test
    public void acceptsHasEmailsForEmailAgreement() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.getTenants().stream().forEach(tenant -> tenant.setEmail("ddd@ddd.com"));
        modelTenancy.setCommunicationsAgreement(CommunicationsAgreement.EMAIL.name());
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, builder);

        // ASSERT
        assertEquals(builder.build().getIssues().size(), 0);
    }

    @Test
    public void rejectsNoEmailsForEmailAgreement() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.getTenants().stream().forEach(tenant -> tenant.setEmail(""));
        modelTenancy.setCommunicationsAgreement(CommunicationsAgreement.EMAIL.name());
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, builder);

        // ASSERT
        assertEquals(builder.build().getIssues().size(), 1);
    }



}
