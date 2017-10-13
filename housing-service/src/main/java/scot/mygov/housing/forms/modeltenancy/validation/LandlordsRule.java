package scot.mygov.housing.forms.modeltenancy.validation;

import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

import java.util.stream.IntStream;

public class LandlordsRule implements ValidationRule<ModelTenancy> {

    private static final String FIELD = "landlords";

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        // must be at least one landlord
        if (modelTenancy.getLandlords().isEmpty()) {
            resultsBuilder.issue(FIELD, "Must specify at least one landlord");
            return;
        }

        // validate each of the landlords
        IntStream.range(0, modelTenancy.getLandlords().size()).forEach(i -> {
            AgentOrLandLord landlord = modelTenancy.getLandlords().get(i);
            String field = FIELD + (i + 1);
            AddressValidator.validate(landlord.getAddress(), "landlord", resultsBuilder);
            ValidationUtil.nonEmpty(landlord, field, resultsBuilder, "name");
            ValidationUtil.validateContactDetails(landlord, field, resultsBuilder);
            ValidationUtil.validateRegistrationNumber(landlord.getRegistrationNumber(),
                    true, FIELD + "-registrationNumber", resultsBuilder);
        });
    }
}
