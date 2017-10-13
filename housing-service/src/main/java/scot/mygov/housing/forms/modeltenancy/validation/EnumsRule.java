package scot.mygov.housing.forms.modeltenancy.validation;

import scot.mygov.housing.forms.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.forms.modeltenancy.model.FurnishingType;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

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
