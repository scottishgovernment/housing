package scot.mygov.housing.forms.modeltenancy.validation;

import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

public class LettingAgentRule implements ValidationRule<ModelTenancy> {

    private static final String FIELD = "lettingAgent";

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        // the letting agent is optional
        if (modelTenancy.getLettingAgent() == null) {
            return;
        }

        // if present then must have valid contact details and a valid registration nunber
        AgentOrLandLord agent =  modelTenancy.getLettingAgent();
        ValidationUtil.nonEmpty(agent, FIELD, resultsBuilder, "name");
        AddressValidator.validate(agent.getAddress(), FIELD, resultsBuilder);
        ValidationUtil.validateContactDetails(agent, FIELD, resultsBuilder);
        ValidationUtil.validateRegistrationNumber(agent.getRegistrationNumber(), false, FIELD + "-registrationNumber", resultsBuilder);
    }
}
