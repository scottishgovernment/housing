package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.Days;
import scot.mygov.housing.modeltenancy.model.FurnishingType;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.housing.modeltenancy.model.Utility;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

/**
 * Created by z418868 on 16/06/2017.
 */
public class EnumsRule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {
        ValidationUtil.validateEnum(modelTenancy.getCommunicationsAgreement(),
                "communicationsAgreement",
                CommunicationsAgreement.class,
                resultsBuilder);

        ValidationUtil.validateEnum(modelTenancy.getFurnishingType(),
                "furnishingType",
                FurnishingType.class,
                resultsBuilder);

        ValidationUtil.validateEnum(modelTenancy.getRentPaymentFrequency(),
                "rentPaymentFrequency",
                RentPaymentFrequency.class,
                resultsBuilder);

        ValidationUtil.validateEnum(modelTenancy.getRentPaymentDay(),
                "rentPaymentDay",
                Days.class,
                resultsBuilder);

        // Note: this field is optional and so an empty list is considered valid
        modelTenancy.getTenantUtilitiesResponsibilities().stream().forEach(utility ->
            ValidationUtil.validateEnum(utility,
                "tenantUtilitiesResponsibilities",
                Utility.class,
                resultsBuilder));
    }
}
