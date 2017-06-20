package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

import java.util.stream.IntStream;

/**
 * Ensure each tenant has at least onf of phone or telephone number;
 */
public class TenantsRule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {
        IntStream.range(0, modelTenancy.getTenants().size()).forEach(i -> {
            // each tenant shoudl have a non empty addreee and valid contact details
            Person tenant = modelTenancy.getTenants().get(i);
            String field = "tenant" + (i + 1);
            ValidationUtil.nonEmpty(tenant, field, resultsBuilder, "name", "address");
            ValidationUtil.validateContactDetails(tenant, "tenant" + (i + 1), resultsBuilder);
        });
    }
}
