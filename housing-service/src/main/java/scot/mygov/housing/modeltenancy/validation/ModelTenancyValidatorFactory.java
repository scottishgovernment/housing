package scot.mygov.housing.modeltenancy.validation;

import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.MandatoryFieldsRule;
import scot.mygov.validation.MoneyFieldsRule;
import scot.mygov.validation.ValidationRule;
import scot.mygov.validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelTenancyValidatorFactory {

    private static final String [] MANDATORY_FIELDS = {
        "propertyType",
        "tenancyStartDate",
        "firstPaymentDate",
        "firstPaymentPeriodStart",
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
    public Validator<ModelTenancy> newInstance() {
        List<ValidationRule> rules = new ArrayList<>();

        Collections.addAll(rules,
                new LandlordsRule(),
                new AtLeastOneTenantRule(),
                new TenantsRule(),
                new GuarantorRule(),
                new LettingAgentRule(),
                new EnumsRule(),
                new HMORule(),
                new ServicesRule(),
                new CommunicationsAgreementRule(),
                new MandatoryFieldsRule(MANDATORY_FIELDS),
                new MoneyFieldsRule(MONEY_FIELDS),
                new DepositSchemeAdministratorRule());
        return new Validator(rules);
    }
}
