package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

/**
 * Created by z418868 on 19/06/2017.
 */
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
        Assert.assertTrue(b.build().getIssues().isEmpty());
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
        Assert.assertNotNull(b.build().getIssues().get("service1-name"));
    }
}

