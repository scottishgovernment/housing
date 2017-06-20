package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

/**
 * Created by z418868 on 16/06/2017.
 */
public class AtLeastOneTenantRule implements ValidationRule<ModelTenancy> {
    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        if (modelTenancy.getTenants().isEmpty()) {
            resultsBuilder.issue("tenants", "tenancy should have at least one tenant");
        }
    }
}
