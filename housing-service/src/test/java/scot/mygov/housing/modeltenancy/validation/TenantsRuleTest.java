package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

/**
 * Created by z418868 on 19/06/2017.
 */
public class TenantsRuleTest {

    private ObjectMother om = new ObjectMother();
    private TenantsRule rule = new TenantsRule();

    @Test
    public void acceptsValidTenants() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        Assert.assertTrue(b.build().getIssues().isEmpty());
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
        Assert.assertNotNull(b.build().getIssues().get("tenant1-name"));
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
        Assert.assertNotNull(b.build().getIssues().get("tenant1-address"));
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
        Assert.assertNotNull(b.build().getIssues().get("tenant1"));
    }
}
