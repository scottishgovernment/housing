package scot.mygov.housing.forms.modeltenancy.validation;

import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.MandatoryFieldsRule;
import scot.mygov.validation.MoneyFieldsRule;
import scot.mygov.validation.ValidationRule;
import scot.mygov.validation.Validator;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

public class ModelTenancyValidatorFactory {

    private static final String [] MANDATORY_FIELDS = {
        "propertyType",
        "tenancyStartDate",
        "firstPaymentDate",
        "firstPaymentPeriodEnd",
        "rentPaymentMethod",
        "rentPaymentDayOrDate",
        "rentPaymentSchedule",
        "depositAmount",
        "tenancyDepositSchemeAdministrator"
    } ;

    private static final String [] MONEY_FIELDS = {
        "rentAmount",
        "firstPaymentAmount",
        "depositAmount"
    };

    /**
     * Compose the validation rules used to validate ModelTenancy objects.
     */
    public Validator<ModelTenancy> validator(boolean validationEnabled) {
        List<ValidationRule> rules = new ArrayList<>();
        addAll(rules, new DepositSchemeAdministratorRule());

        if (validationEnabled) {
            addAll(rules,
                new LandlordsRule(),
                new AtLeastOneTenantRule(),
                new TenantsRule(),
                new GuarantorRule(),
                new LettingAgentRule(),
                new EnumsRule(),
                new HMORule(),
                new CommunicationsAgreementRule(),
                new MandatoryFieldsRule(MANDATORY_FIELDS),
                new MoneyFieldsRule(MONEY_FIELDS));
        }
        return new Validator(rules);
    }
}
