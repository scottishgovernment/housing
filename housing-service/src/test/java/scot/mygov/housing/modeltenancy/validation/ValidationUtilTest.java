package scot.mygov.housing.modeltenancy.validation;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.housing.postcode.GeoPostcodeSource;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationUtilTest {

    private ObjectMother om = new ObjectMother();

    @Test
    public void rejectsInvalidEmails() {
        // ARRANGE
        List<String> invalidEmails = om.invalidEmails();

        // ACT
        List<String> actual = invalidEmails.stream().filter(ValidationUtil::validEmail).collect(Collectors.toList());

        // ASSERT
        Assert.assertEquals(0, actual.size());
    }

    @Test
    public void acceptsValidEmails() {
        // ARRANGE
        List<String> validEmails = om.validEmails();

        // ACT
        List<String> actual = validEmails.stream().filter(ValidationUtil::validEmail).collect(Collectors.toList());

        // ASSERT
        Assert.assertEquals(validEmails.size(), actual.size());
    }

    @Test
    public void nonEmptyRejectEmptyFields() {
        // ARRANGE
        Person input = om.emptyPerson();
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        ValidationUtil.nonEmpty(input, "field", resultsBuilder, "name", "address");
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), 2);
    }


    @Test
    public void nonEmptyRejectUnknownFields() {
        // ARRANGE
        Person input = om.validPerson();
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        ValidationUtil.nonEmpty(input, "field", resultsBuilder, "name", "address", "nosuchproperty");
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), 1);
    }

    @Test
    public void nonEmptyAccptsNonEmptyFields() {
        // ARRANGE
        Person input = om.validPerson();
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        ValidationUtil.nonEmpty(input, "field", resultsBuilder, "name", "address");
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), 0);
    }

    @Test
    public void validateContactDetailsAcceptsValidPeople() {

        // ARRANGE
        List<Person> validPeople = om.validPeople();
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        validPeople.stream().forEach(person -> ValidationUtil.validateContactDetails(person, "field", resultsBuilder));
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), 0);
    }

    @Test
    public void validateContactDetailsRejectsInvalidPeople() {

        // ARRANGE
        List<Person> invalidPeople = om.invalidPeople();
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        invalidPeople.stream().forEach(person -> ValidationUtil.validateContactDetails(person, RandomStringUtils.random(10), resultsBuilder));
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), invalidPeople.size());
    }

    @Test
    public void validateRegistrationNumberRejectsInvalidRegistrationNumbers() {

        // ARRANGE
        List<String> invalid = om.invalidRegNumbers();
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        invalid.stream().forEach(
                number -> ValidationUtil.validateRegistrationNumber(number, true, RandomStringUtils.random(10), resultsBuilder));
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), invalid.size());
    }

    @Test
    public void validateRegistrationNumberAcceptsRegistrationNumbers() {

        // ARRANGE
        List<String> valid = om.validRegNumbers();
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        valid.stream().forEach(
                number -> ValidationUtil.validateRegistrationNumber(number, true, RandomStringUtils.random(10), resultsBuilder));
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), 0);
    }

    @Test
    public void validateRegistrationNumberAcceptsPendingIfFlagSet() {

        // ARRANGE
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        ValidationUtil.validateRegistrationNumber("Pending", true, RandomStringUtils.random(10), resultsBuilder);
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), 0);
    }

    @Test
    public void validateRegistrationNumberRejectsPendingIfFlagNotSet() {

        // ARRANGE
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();

        // ACT
        ValidationUtil.validateRegistrationNumber("Pending", false, RandomStringUtils.random(10), resultsBuilder);
        ValidationResults results = resultsBuilder.build();

        // ASSERT
        Assert.assertEquals(results.getIssues().entrySet().size(), 1);
    }

    @Test
    public void validPostCodes() {
        List<String> inputs = new ArrayList<>();
        Collections.addAll(inputs, "EH104AX", "EH10 4AX", "eh104ax", "eh10 4ax", "B1 2HB");
        List<String> outputs = inputs.stream().filter(in -> ValidationUtil.validPostcode(in)).collect(Collectors.toList());
        junit.framework.Assert.assertEquals(inputs, outputs);
    }

    @Test
    public void invalidPostCodes() {
        List<String> inputs = new ArrayList<>();
        Collections.addAll(inputs, "", "aaa", "EH10-$AX");
        List<String> outputs = inputs.stream().filter(in -> !ValidationUtil.validPostcode(in)).collect(Collectors.toList());
        Assert.assertEquals(inputs, outputs);
    }

}
