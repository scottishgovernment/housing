package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HMORuleTest {

    private ValidationRule<ModelTenancy> rule = new HMORule();
    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();

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
        assertTrue(b.build().getIssues().isEmpty());
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
        assertTrue(b.build().getIssues().isEmpty());
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
        assertFalse(b.build().getIssues().isEmpty());
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
        assertFalse(b.build().getIssues().isEmpty());
    }
}
