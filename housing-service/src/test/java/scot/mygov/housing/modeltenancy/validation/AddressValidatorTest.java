package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.Address;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressValidatorTest {

    private ObjectMother om = new ObjectMother();

    @Test
    public void accceptsValidAddresses() {
        // ARRANGE
        List<Address> input = new ArrayList<>(om.validAddresses());

        // ACT

        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();
        input.stream().forEach(address -> AddressValidator.validate(address, "field", resultsBuilder));


        // ASSERT
        Assert.assertTrue(resultsBuilder.build().getIssues().isEmpty());
    }

    @Test
    public void rejectsInvalidAddresses() {
        // ARRANGE
        List<Address> input = new ArrayList<>(om.invalidAddresses());

        // ACT
        List<Address> rejected = input.stream()
                .filter(address -> {
                    ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();
                    AddressValidator.validate(address, "field", resultsBuilder);


                    return !resultsBuilder.build().getIssues().isEmpty();
                })
                .collect(Collectors.toList());


        // ASSERT
        Assert.assertEquals(rejected.size(), input.size());
    }
}
