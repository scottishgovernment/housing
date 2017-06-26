package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.Guarantor;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.validation.ValidationRule;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toMap;
import static java.util.function.Function.identity;

/**
 * Created by z418868 on 20/06/2017.
 */
public class GuarantorRule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {
        Map<String, Person> tenantsByName = modelTenancy.getTenants()
                .stream()
                .collect(toMap(Person::getName, identity()));

        IntStream.range(0, modelTenancy.getGuarantors().size()).forEach(i -> {
            Guarantor guarantor = modelTenancy.getGuarantors().get(i);
            String fieldBase = "guarantor" + (i+1);

            // each guarantor must have a name and address
            ValidationUtil.nonEmpty(guarantor, fieldBase, resultsBuilder, "name");
            AddressValidator.validate(guarantor.getAddress(), fieldBase, resultsBuilder);

            // must refer to at least one tenant
            if (guarantor.getTenantNames().isEmpty()) {
                resultsBuilder.issue(fieldBase + "-tenantnames", "Mandatory");
            }

            // each guarantor must refer to a tenant by name
            guarantor.getTenantNames().stream().forEach(tenantName -> {
                if (!tenantsByName.containsKey(tenantName)) {
                    resultsBuilder.issue(fieldBase + "-tenantname", "No such tenent");
                }
            });
        });
    }
}
