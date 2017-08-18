package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertEquals;

public class AtLeastOneTenantRuleTest {

    private ObjectMother om = new ObjectMother();
    private AtLeastOneTenantRule rule = new AtLeastOneTenantRule();

    @Test
    public void acceptsOneTenant() {

        // ARRANGE
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(tenancyWithTenantCount(1), b);
        ;
        // ASSERT
        assertEquals(b.build().getIssues().size(), 0);

    }

    @Test
    public void rejectNoTenants() {

        // ARRANGE
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(tenancyWithTenantCount(0), b);

        // ASSERT
        assertEquals(b.build().getIssues().size(), 1);

    }

    @Test
    public void acceptsMultipleTenants() {

        // ARRANGE
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(tenancyWithTenantCount(5), b);

        // ASSERT
        assertEquals(b.build().getIssues().size(), 0);

    }

    private ModelTenancy tenancyWithTenantCount(int count) {
        ModelTenancy m = new ModelTenancy();
        for (int i =0; i<count; i++) {
            m.getTenants().add(om.personWithAllFields());
        }
        return m;
    }
}
