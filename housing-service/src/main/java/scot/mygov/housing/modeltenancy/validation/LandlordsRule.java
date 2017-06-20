package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

import java.util.stream.IntStream;

/**
 * Created by z418868 on 16/06/2017.
 */
public class LandlordsRule implements ValidationRule<ModelTenancy> {

    private static final String FIELD = "landlords";

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        // must be at least one landlord
        if (modelTenancy.getLandlords().isEmpty()) {
            resultsBuilder.issue(FIELD, "Must specify at least one landlord");
            return;
        }

        // cannot have more than 2 landlords
        if (modelTenancy.getLandlords().size() > 2) {
            resultsBuilder.issue(FIELD, "More than 2 landlords");
            return;
        }

        // validate each of the landlords
        IntStream.range(0, modelTenancy.getLandlords().size()).forEach(i -> {
            AgentOrLandLord landlord = modelTenancy.getLandlords().get(i);
            String field = FIELD + (i + 1);
            ValidationUtil.nonEmpty(landlord, field, resultsBuilder, "name", "address");
            ValidationUtil.validateContactDetails(landlord, field, resultsBuilder);
            ValidationUtil.validateRegistrationNumber(landlord.getRegistrationNumber(),
                    true, FIELD + "-registrationNumber", resultsBuilder);
        });
    }
}
