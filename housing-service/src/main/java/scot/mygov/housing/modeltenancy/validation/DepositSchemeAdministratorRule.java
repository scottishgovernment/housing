package scot.mygov.housing.modeltenancy.validation;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.modeltenancy.DepositSchemeAdministrators;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

public class DepositSchemeAdministratorRule implements ValidationRule<ModelTenancy> {

    private final DepositSchemeAdministrators administrators = new DepositSchemeAdministrators();

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        // the field is not mandatory
        if (StringUtils.isEmpty(modelTenancy.getTenancyDepositSchemeAdministrator())) {
            return;
        }

        // if they have specifeid one it must be one we recognise
        if (administrators.forName(modelTenancy.getTenancyDepositSchemeAdministrator()) == null) {
            resultsBuilder.issue("tenancyDepositSchemeAdministrator", "Unrecognised scheme administrator");
        }
    }
}
