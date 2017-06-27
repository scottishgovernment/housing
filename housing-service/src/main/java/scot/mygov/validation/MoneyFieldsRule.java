package scot.mygov.validation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by z418868 on 19/06/2017.
 */
public class MoneyFieldsRule<T> implements ValidationRule<T> {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyFieldsRule.class);

    private static final String REGEX ="^[0-9]+\\.[0-9-]{2}+$";

    private List<String> fields;

    public MoneyFieldsRule(String... fields) {
        this.fields = Arrays.asList(fields);
    }

    public void validate(T model, ValidationResultsBuilder resultsBuilder) {
        for (String field : fields) {
            validateField(model, field, resultsBuilder);
        }
    }

    private void validateField(T model, String field, ValidationResultsBuilder builder) {
        String value = null;
        try {
            value = BeanUtils.getProperty(model, field);
            if (StringUtils.isEmpty(value) || !value.matches(REGEX)) {
                builder.issue(field, "Invalid monetary value: " + value);
            }
        } catch (Exception e) {
            builder.issue(field, "Invalid field: " + field);
            LOG.warn("Unknown property", e);
        }
    }

}
