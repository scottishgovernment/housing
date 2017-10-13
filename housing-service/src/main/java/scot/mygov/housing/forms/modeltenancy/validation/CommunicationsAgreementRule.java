package scot.mygov.housing.forms.modeltenancy.validation;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.forms.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

public class CommunicationsAgreementRule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {
        String agreement = modelTenancy.getCommunicationsAgreement();
        if (StringUtils.isEmpty(agreement)) {
            return;
        }

        if (agreement.equals(CommunicationsAgreement.EMAIL.name())) {
            // if they have chosen email then all tenants should have an email address
            long noEmailCount = modelTenancy.getTenants()
                    .stream()
                    .filter(tenant -> StringUtils.isEmpty(tenant.getEmail()))
                    .count();

            if (noEmailCount > 0) {
                resultsBuilder.issue("communicationsAgreement",
                        "All tenants must have an email address for this communications agreement");
            }
        }
    }
}
