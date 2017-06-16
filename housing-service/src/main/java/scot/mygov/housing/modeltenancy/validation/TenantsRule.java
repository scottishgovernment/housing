package scot.mygov.housing.modeltenancy.validation;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.Person;

import java.util.stream.IntStream;

/**
 * Ensure each tenant has at least onf of phone or telephone number;
 */
public class TenantsRule implements Rule  {

    public void validate(ModelTenancy modelTenancy, ModelTenancyValidator.ResultsBuilder resultsBuilder) {
        IntStream.range(0, modelTenancy.getTenants().size()).forEach(i -> {
            Person tenant = modelTenancy.getTenants().get(i);
            ValidationUtil.validateContactDetails(tenant, "tenant" + (i + 1), resultsBuilder);
        });
    }
}
