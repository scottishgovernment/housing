package scot.mygov.housing.forms.modeltenancy.validation;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

public class HMORule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        if ("true".equals(modelTenancy.getHmoProperty())) {
            if (StringUtils.isEmpty(modelTenancy.getHmo24ContactNumber())) {
                resultsBuilder.issue("hmoContactNumber", "Required for HMO properties");
            }

            if (modelTenancy.getHmoRegistrationExpiryDate() == null) {
                resultsBuilder.issue("hmoExpirationDate", "Required for HMO properties");
            }
        }

        if ("false".equals(modelTenancy.getHmoProperty())) {
            if (!StringUtils.isEmpty(modelTenancy.getHmo24ContactNumber())) {
                resultsBuilder.issue("hmoContactNumber", "Not allowed for non-HMO properties");
            }

            if (modelTenancy.getHmoRegistrationExpiryDate() != null) {
                resultsBuilder.issue("hmoExpirationDate", "Not allowed for non-HMO properties");
            }
        }
    }
}
