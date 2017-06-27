package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.Collections;

/**
 * Created by z418868 on 20/06/2017.
 */
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
        Assert.assertTrue(b.build().getIssues().isEmpty());
    }

    @Test
    public void acceptsNoGuarentors() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.setGuarantors(Collections.emptyList());
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        Assert.assertTrue(b.build().getIssues().isEmpty());
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
        Assert.assertFalse(b.build().getIssues().isEmpty());
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
        Assert.assertFalse(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsGuarentorsWithNoTenantNames() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.getGuarantors().get(0).setTenantNames(Collections.emptyList());
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        Assert.assertFalse(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsGuarentorsWithTenantNameNotInTenants() {
        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        modelTenancy.getGuarantors().get(0).setTenantNames(Collections.singletonList("not a tenant name"));
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(modelTenancy, b);

        // ASSERT
        Assert.assertFalse(b.build().getIssues().isEmpty());
    }

    private Person tenantWithNoGuarentor() {
        Person p = new Person();
        p.setName("qqq");
        p.setAddress(om.validAddress());
        p.setTelephone("111");
        return p;
    }
}
