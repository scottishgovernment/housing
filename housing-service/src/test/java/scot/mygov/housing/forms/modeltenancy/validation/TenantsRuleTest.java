package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TenantsRuleTest {

    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();
    private TenantsRule rule = new TenantsRule();

    @Test
    public void acceptsValidTenants() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertTrue(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsTenantsWithEmptyName() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.getTenants().get(0).setName("");
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertNotNull(b.build().getIssues().get("tenant1-name"));
    }

    @Test
    public void rejectsTenantsWithNullAddress() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.getTenants().get(0).setAddress(null);
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertNotNull(b.build().getIssues().get("tenant1-address"));
    }

    @Test
    public void rejectsTenantsWithInvalidContactDetails() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.getTenants().get(0).setTelephone("");
        modelTenancy.getTenants().get(0).setEmail("");
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertNotNull(b.build().getIssues().get("tenant1"));
    }
}
