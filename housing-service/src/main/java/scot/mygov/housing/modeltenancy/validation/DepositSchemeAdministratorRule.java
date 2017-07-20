package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.DepositSchemeAdministrators;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

public class DepositSchemeAdministratorRule implements ValidationRule<ModelTenancy> {

    private final DepositSchemeAdministrators administrators = new DepositSchemeAdministrators();

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        if (administrators.forName(modelTenancy.getTenancyDepositSchemeAdministrator()) == null) {
            resultsBuilder.issue("tenancyDepositSchemeAdministrator", "Unrecognised scheme adminsitrator");
        }
    }
}
