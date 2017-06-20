package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.ValidationResultsBuilder;
import scot.mygov.validation.ValidationRule;

/**
 * Created by z418868 on 16/06/2017.
 */
public class LettingAgentRule implements ValidationRule<ModelTenancy> {

    private static final String FIELD = "lettingAgent";

    public void validate(ModelTenancy modelTenancy, ValidationResultsBuilder resultsBuilder) {

        // the letting agent is optional
        if (modelTenancy.getLettingAgent() == null) {
            return;
        }

        // if present then must have valid contact details and a valid registration nunber
        AgentOrLandLord agent =  modelTenancy.getLettingAgent();
        ValidationUtil.nonEmpty(agent, FIELD, resultsBuilder, "name", "address");
        ValidationUtil.validateContactDetails(agent, FIELD, resultsBuilder);
        ValidationUtil.validateRegistrationNumber(agent.getRegistrationNumber(), false, FIELD + "-registrationNumber", resultsBuilder);
    }
}
