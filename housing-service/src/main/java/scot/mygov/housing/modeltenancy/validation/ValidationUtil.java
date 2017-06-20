package scot.mygov.housing.modeltenancy.validation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.Arrays;

/**
 * Created by z418868 on 16/06/2017.
 */
public class ValidationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationUtil.class);

    private static final String EMAIL_REGEX ="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    private static final String LANDLORD_REF_REGEX ="^[0-9]{6}\\/[0-9]{3}\\/[0-9]{5}+$";

    /**
     * prevent instance creation.
     */
    private ValidationUtil() {
    }

    public static boolean validEmail(String email) {
        return !StringUtils.isEmpty(email) && email.matches(EMAIL_REGEX);
    }

    public static void nonEmpty(Object bean, String field, ValidationResultsBuilder resultsBuilder, String ...requiredFields) {

        Arrays.stream(requiredFields).forEach(requiredField -> {
            String specificFeild = field + "-" + requiredField;
            String value = null;
            try {
                value = BeanUtils.getProperty(bean, requiredField);
            } catch (Exception e) {
                // logg the error
                LOG.warn("Invalid proerty for bean", e);
            } finally {
                if (StringUtils.isEmpty(value)) {
                    resultsBuilder.issue(specificFeild, "Required");
                }
            }
        });
    }

    public static void validateContactDetails(Person person, String field, ValidationResultsBuilder resultsBuilder) {

        // should have at least one contact method.
        if (StringUtils.isEmpty(person.getEmail()) && StringUtils.isEmpty(person.getTelephone())) {
            resultsBuilder.issue(field, "Must provide at least one of email or telephone");
        }

        // if they have provided an email then it should be valid
        if (!StringUtils.isEmpty(person.getEmail()) && !ValidationUtil.validEmail(person.getEmail())) {
            resultsBuilder.issue(field, "Invalid email");
        }
    }

    public static void validateRegistrationNumber(String registrationNumber,
                                                  boolean allowPending,
                                                  String field,
                                                  ValidationResultsBuilder resultsBuilder) {
        if (StringUtils.isEmpty(registrationNumber)) {
            resultsBuilder.issue(field, "Registration number is required");
            return;
        }

        String msg = "Registration number must be in the format _ _ _ _ _ _ / _ _ _ / _ _ _ _ _";
        if (allowPending && "Pending".equals(registrationNumber)) {
            return;
        }

        if (allowPending) {
            msg += " or 'Pending'";
        }

        if (!registrationNumber.matches(LANDLORD_REF_REGEX)) {
            resultsBuilder.issue(field, msg);
        }
    }

    public static void validateEnum(String value, String field, Class e, ValidationResultsBuilder resultsBuilder) {
        if (!EnumUtils.isValidEnum(e, value)) {
            resultsBuilder.issue(field, "Invalid value");
        }
    }
}
