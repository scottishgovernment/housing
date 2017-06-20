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

    public MoneyFieldsRule(String ... fields) {
        this.fields = Arrays.asList(fields);
    }


    public void validate(T model, ValidationResultsBuilder resultsBuilder) {
        fields.stream().forEach(field -> {
            String value = null;
            try {

                Object property = BeanUtils.getProperty(model, field);
                if (property != null) {
                    value = property.toString();
                }
            } catch (Exception e) {
                LOG.warn("Unknown property", e);
            } finally {
                if (StringUtils.isEmpty(value) || !value.matches(REGEX)) {
                    resultsBuilder.issue(field, "Invalid monetary value");
                }
            }
        });
    }
}