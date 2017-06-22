package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.FurnishingType;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.RentPaymentFrequency;
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
    }
}
