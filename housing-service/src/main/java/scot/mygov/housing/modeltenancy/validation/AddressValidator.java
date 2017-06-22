package scot.mygov.housing.modeltenancy.validation;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.modeltenancy.model.Address;
import scot.mygov.validation.ValidationResultsBuilder;

/**
 * Created by z418868 on 21/06/2017.
 */
public class AddressValidator {

    private AddressValidator() {
        // prevent creation
    }

    public static void validate(Address address, String field, ValidationResultsBuilder resultsBuilder) {
        if (address == null) {
            resultsBuilder.issue(field + "-address", "Address is mandatory");
            return;
        }

        if (StringUtils.isEmpty(address.getAddressLine1())) {
            resultsBuilder.issue(field + "-addressline1", "Address must specify at least one line");
        }

        // validate the postcode ... need postcode source.
        if (StringUtils.isEmpty(address.getPostcode())) {
            resultsBuilder.issue(field + "-postcode", "Mandatory");
        } else {
            if (!ValidationUtil.validPostcode(address.getPostcode())) {
                resultsBuilder.issue(field + "-postcode", "Invalid postcode");
            }
        }
    }
}