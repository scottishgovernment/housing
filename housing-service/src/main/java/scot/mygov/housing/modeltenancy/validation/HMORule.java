package scot.mygov.housing.modeltenancy.validation;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationRule;
import scot.mygov.validation.ValidationResultsBuilder;

/**
 * Created by z418868 on 19/06/2017.
 */
public class HMORule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {
        if (modelTenancy.isHmoProperty()) {
            if (StringUtils.isEmpty(modelTenancy.getHmo24ContactNumber())) {
                resultsBuilder.issue("hmoContactNumber", "Required for HMO properties");
            }

            if (modelTenancy.getHmoRegistrationExpiryDate() == null) {
                resultsBuilder.issue("hmoExpirationDate", "Required for HMO properties");
            }
        } else {
            if (!StringUtils.isEmpty(modelTenancy.getHmo24ContactNumber())) {
                resultsBuilder.issue("hmoContactNumber", "Not required for non-HMO properties");
            }

            if (modelTenancy.getHmoRegistrationExpiryDate() != null) {
                resultsBuilder.issue("hmoExpirationDate", "Not required for non-HMO properties");
            }
        }
    }
}
