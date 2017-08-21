package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.Collections;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GuarantorRuleTest {
    private ObjectMother om = new ObjectMother();
    private GuarantorRule rule = new GuarantorRule();

    @Test
    public void acceptsValidGuarentors() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertTrue(b.build().getIssues().isEmpty());
    }

    @Test
    public void acceptsNoGuarentors() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.setGuarantors(emptyList());
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertTrue(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsGuarentorsWithNoName() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.getGuarantors().get(0).setName(null);
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertFalse(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsGuarentorsWithNoAddress() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.getGuarantors().get(0).setAddress(null);
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertFalse(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsGuarentorsWithNoTenantNames() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.getGuarantors().get(0).setTenantNames(emptyList());
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertFalse(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsGuarentorsWithTenantNameNotInTenants() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.getGuarantors().get(0).setTenantNames(singletonList("not a tenant name"));
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertFalse(b.build().getIssues().isEmpty());
    }
}
