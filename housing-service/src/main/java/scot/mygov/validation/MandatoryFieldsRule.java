package scot.mygov.validation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MandatoryFieldsRule<T> implements ValidationRule<T> {

    private static final Logger LOG = LoggerFactory.getLogger(MandatoryFieldsRule.class);

    private List<String> fields;

    public MandatoryFieldsRule(String... fields) {
        this.fields = Arrays.asList(fields);
    }

    public void validate(T model, ValidationResultsBuilder resultsBuilder) {

        fields.stream().forEach(mandatoryField -> {
            String value = null;
            try {

                value = BeanUtils.getProperty(model, mandatoryField);
                if (StringUtils.isEmpty(value)) {
                    resultsBuilder.issue(mandatoryField, "Required");
                }
            } catch (Exception e) {
                LOG.warn("Unknown property", e);
                resultsBuilder.issue(mandatoryField, "Invalid field");
            }
        });
    }

}
