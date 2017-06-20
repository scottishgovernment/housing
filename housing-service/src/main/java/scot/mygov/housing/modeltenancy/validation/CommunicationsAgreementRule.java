package scot.mygov.housing.modeltenancy.validation;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

/**
 * Created by z418868 on 20/06/2017.
 */
public class CommunicationsAgreementRule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {
        if (modelTenancy.getCommunicationsAgreement().equals(CommunicationsAgreement.EMAIL.name())) {
            // if they have chsen email then all tenants should have an email address
            long noEmailCount = modelTenancy.getTenants()
                    .stream()
                    .filter(tenant -> StringUtils.isEmpty(tenant.getEmail()))
                    .count();

            if (noEmailCount > 0) {
                resultsBuilder.issue("communicationsAgreement",
                        "All tenents must have an email address for this communications agreement");
            }
        }
    }
}
