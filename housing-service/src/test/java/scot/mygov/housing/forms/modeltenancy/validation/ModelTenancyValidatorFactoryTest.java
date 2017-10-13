package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationException;
import scot.mygov.validation.Validator;

public class ModelTenancyValidatorFactoryTest {

    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();

    @Test
    public void validationEnabledCanValidataValidTenancy() throws ValidationException {
        ModelTenancyValidatorFactory sut = new ModelTenancyValidatorFactory();
        Validator<ModelTenancy> validator = sut.validator(true);

        ModelTenancy model = new ModelTenancyObjectMother().anyTenancy();
        try {
            validator.validate(model);
        } catch (ValidationException e) {
            throw e;
        }
    }

    @Test(expected=ValidationException.class)
    public void validationEnabledCannotValidateInvalidTenancy() throws ValidationException {
        ModelTenancyValidatorFactory sut = new ModelTenancyValidatorFactory();
        Validator<ModelTenancy> validator = sut.validator(true);

        ModelTenancy model = invalidTenancy();
        try {
            validator.validate(model);
        } catch (ValidationException e) {
            throw e;
        }
    }

    @Test
    public void validationDisabledCanValidateInvalidTenancy() throws ValidationException {
        ModelTenancyValidatorFactory sut = new ModelTenancyValidatorFactory();
        Validator<ModelTenancy> validator = sut.validator(false);

        ModelTenancy model = invalidTenancy();
        // property
        model.setPropertyType(null);
        try {
            validator.validate(model);
        } catch (ValidationException e) {
            throw e;
        }
    }

    private ModelTenancy invalidTenancy() {
        ModelTenancy tenancy = new ModelTenancyObjectMother().anyTenancy();
        tenancy.setPropertyType(null);
        return tenancy;
    }
}
