package scot.mygov.housing.modeltenancy.validation;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class EnumsRuleTest {

    private ObjectMother om = new ObjectMother();
    private EnumsRule rule = new EnumsRule();

    @Test
    public void acceptsValidValues() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        List<String> values = Arrays.asList(CommunicationsAgreement.values())
                .stream()
                .map(CommunicationsAgreement::name)
                .collect(toList());
        ValidationResultsBuilder builder = new ValidationResultsBuilder();


        // ACT
        values.stream().forEach(value -> {
            modelTenancy.setCommunicationsAgreement(value);
            rule.validate(modelTenancy, builder);
        });

        // ASSERT
        assertEquals(builder.build().getIssues().size(), 0);
    }

    @Test
    public void rejectsInvalidCommunicationsAgreementValues() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        List<String> values = new ArrayList<>();
        addAll(values, "blah", null, "");

        // ACT
        values.stream().forEach(value -> {
            ValidationResultsBuilder builder = new ValidationResultsBuilder();
            modelTenancy.setCommunicationsAgreement(value);
            rule.validate(modelTenancy, builder);

            // ASSERT
            assertEquals(builder.build().getIssues().size(), 1);
        });
    }

    @Test
    public void rejectsInvalidFurnishTypeValues() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        List<String> values = new ArrayList<>();
        addAll(values, "blah", null, "");

        // ACT
        values.stream().forEach(value -> {
            ValidationResultsBuilder builder = new ValidationResultsBuilder();
            modelTenancy.setFurnishingType(value);
            rule.validate(modelTenancy, builder);

            // ASSERT
            assertEquals(builder.build().getIssues().size(), 1);
        });
    }

    @Test
    public void rejectsInvalidRentPaymentFrwquencyValues() {
        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        List<String> values = new ArrayList<>();
        addAll(values, "blah", null, "");

        // ACT
        values.stream().forEach(value -> {
            ValidationResultsBuilder builder = new ValidationResultsBuilder();
            modelTenancy.setRentPaymentFrequency(value);
            rule.validate(modelTenancy, builder);

            // ASSERT
            assertEquals(builder.build().getIssues().size(), 1);
        });
    }
}
