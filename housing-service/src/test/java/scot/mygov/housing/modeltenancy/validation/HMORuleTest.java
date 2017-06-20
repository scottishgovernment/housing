package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationRule;
import scot.mygov.validation.ValidationResultsBuilder;

import java.time.LocalDate;

/**
 * Created by z418868 on 19/06/2017.
 */
public class HMORuleTest {

    private ValidationRule<ModelTenancy> rule = new HMORule();
    private ObjectMother om = new ObjectMother();

    @Test
    public void acceptsHMOPropertyWithFields() {
        // ARRANGE
        ModelTenancy model = om.anyTenancy();
        model.setHmoProperty(true);
        model.setHmo24ContactNumber("111");
        model.setHmoRegistrationExpiryDate(LocalDate.now());
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(model, b);

        // ASSERT
        Assert.assertTrue(b.build().getIssues().isEmpty());
    }

    @Test
    public void acceptsNonHMOPropertyWithoutFields() {
        // ARRANGE
        ModelTenancy model = om.anyTenancy();
        model.setHmoProperty(false);
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(model, b);

        // ASSERT
        Assert.assertTrue(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsHMOPropertyWithoutFields() {
        // ARRANGE
        ModelTenancy model = om.anyTenancy();
        model.setHmoProperty(true);
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(model, b);

        // ASSERT
        Assert.assertFalse(b.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsNonHMOPropertyWithFields() {
        // ARRANGE
        ModelTenancy model = om.anyTenancy();
        model.setHmoProperty(false);
        model.setHmo24ContactNumber("111");
        model.setHmoRegistrationExpiryDate(LocalDate.now());
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(model, b);

        // ASSERT
        Assert.assertFalse(b.build().getIssues().isEmpty());
    }
}
