package scot.mygov.housing.forms.modeltenancy.validation;

import scot.mygov.housing.forms.modeltenancy.model.Guarantor;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class GuarantorRule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {
        Map<String, Person> tenantsByName = modelTenancy.getTenants()
                .stream()
                .collect(toMap(Person::getName, identity()));

        int i = 1;
        for (Guarantor guarantor : modelTenancy.getGuarantors()) {
            String fieldBase = "guarantor" + i++;

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
                    resultsBuilder.issue(fieldBase + "-tenantname", "No such tenant");
                }
            });
        }
    }
}
