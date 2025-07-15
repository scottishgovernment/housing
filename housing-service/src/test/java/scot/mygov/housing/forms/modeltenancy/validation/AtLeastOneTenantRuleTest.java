package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertEquals;

public class AtLeastOneTenantRuleTest {

    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();
    private AtLeastOneTenantRule rule = new AtLeastOneTenantRule();

    @Test
    public void acceptsOneTenant() {

        // ARRANGE
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(tenancyWithTenantCount(1), b);

        // ASSERT
        assertEquals(0, b.build().getIssues().size());
    }

    @Test
    public void rejectNoTenants() {

        // ARRANGE
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(tenancyWithTenantCount(0), b);

        // ASSERT
        assertEquals(1, b.build().getIssues().size());
    }

    @Test
    public void acceptsMultipleTenants() {

        // ARRANGE
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(tenancyWithTenantCount(5), b);

        // ASSERT
        assertEquals(0, b.build().getIssues().size());
    }

    private ModelTenancy tenancyWithTenantCount(int count) {
        ModelTenancy m = new ModelTenancy();
        for (int i =0; i<count; i++) {
            m.getTenants().add(om.personWithAllFields());
        }
        return m;
    }
}
