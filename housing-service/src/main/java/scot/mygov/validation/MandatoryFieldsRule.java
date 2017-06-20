package scot.mygov.validation;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by z418868 on 16/06/2017.
 */
public class MandatoryFieldsRule<T> implements ValidationRule<T> {

    private static final Logger LOG = LoggerFactory.getLogger(MandatoryFieldsRule.class);

    private List<String> fields;

    public MandatoryFieldsRule(String ... fields) {
        this.fields = Arrays.asList(fields);
    }

    public void validate(T model, ValidationResultsBuilder resultsBuilder) {

        fields.stream().forEach(mandatoryField -> {
            String value = null;
            try {

                Object property = BeanUtils.getProperty(model, mandatoryField);
                if (property != null) {
                    value = property.toString();
                }
            } catch (Exception e) {
                LOG.warn("Unknown property", e);
            } finally {
                if (isEmpty(value)) {
                    resultsBuilder.issue(mandatoryField, "Required");
                }
            }
        });
    }

    private boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        if (str.length() == 0) {
            return true;
        }

        return false;
    }
}
