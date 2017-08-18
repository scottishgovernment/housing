package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ServicesRuleTest {

    private ObjectMother om = new ObjectMother();
    private ServicesRule rule = new ServicesRule();

    @Test
    public void acceptsValidServices() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertTrue(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsServiceWithEmptyName() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.getServicesIncludedInRent().get(0).setName("");
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        assertNotNull(b.build().getIssues().get("service1-name"));
    }
}

