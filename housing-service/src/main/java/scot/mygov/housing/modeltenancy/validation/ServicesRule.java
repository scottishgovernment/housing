package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.Service;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

import java.util.stream.IntStream;

/**
 * Created by z418868 on 26/06/2017.
 */
public class ServicesRule implements ValidationRule<ModelTenancy> {

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        IntStream.range(0, modelTenancy.getServicesIncludedInRent().size()).forEach(i -> {
            // each service should have a name
            Service service = modelTenancy.getServicesIncludedInRent().get(i);
            String field = "service" + (i + 1);
            ValidationUtil.nonEmpty(service, field, resultsBuilder, "name");
        });
    }
}
