package scot.mygov.housing.forms.modeltenancy.validation;

import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

public class AtLeastOneTenantRule implements ValidationRule<ModelTenancy> {
    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        if (modelTenancy.getTenants().isEmpty()) {
            resultsBuilder.issue("tenants", "tenancy should have at least one tenant");
        }
    }
}
