package scot.mygov.housing.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationException;
import scot.mygov.validation.Validator;

/**
 * Created by z418868 on 19/06/2017.
 */
public class ModelTenancyValidatorFactoryTest {

    private ObjectMother om = new ObjectMother();

    @Test
    public void canBuildValidator() throws ValidationException {
        Validator<ModelTenancy> validator = new ModelTenancyValidatorFactory().newInstance();
        ModelTenancy model = new ObjectMother().anyTenancy();
        try {
            validator.validate(model);
        } catch (ValidationException e) {
            throw e;
        }
    }
}
