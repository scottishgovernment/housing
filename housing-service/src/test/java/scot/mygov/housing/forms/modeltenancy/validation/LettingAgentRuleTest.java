package scot.mygov.housing.forms.modeltenancy.validation;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

import static org.junit.Assert.assertEquals;

public class LettingAgentRuleTest {

    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();
    private LettingAgentRule rule = new LettingAgentRule();

    @Test
    public void aceptsNoAgent() {
        // ARRANGE
        ModelTenancy input = om.tenancyWithNoAgent();
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(results.getIssues().size(), 0);
    }

    @Test
    public void rejectAgentWithNoName() {
        // ARRANGE
        ModelTenancy input = om.anyTenancy();
        input.getLettingAgent().setName(null);
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(results.getIssues().size(), 1);
    }

    @Test
    public void rejectAgentWithNoAddress() {
        // ARRANGE
        ModelTenancy input = om.anyTenancy();;
        input.getLettingAgent().setAddress(null);
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(results.getIssues().size(), 1);
    }

    @Test
    public void rejectAgentWithNoContactInfo() {
        // ARRANGE
        ModelTenancy input = om.anyTenancy();;
        ValidationResultsBuilder builder = new ValidationResultsBuilder();
        input.getLettingAgent().setEmail(null);
        input.getLettingAgent().setTelephone(null);

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(results.getIssues().size(), 1);
    }

    @Test
    public void rejectAgentWithInvalidRegNumber() {
        // ARRANGE
        ModelTenancy input = om.anyTenancy();
        input.getLettingAgent().setRegistrationNumber(om.invalidRegNumbers().get(0));
        ValidationResultsBuilder builder = new ValidationResultsBuilder();

        // ACT
        rule.validate(input, builder);
        ValidationResults results = builder.build();

        // ASSERT
        assertEquals(results.getIssues().size(), 1);
    }

}
