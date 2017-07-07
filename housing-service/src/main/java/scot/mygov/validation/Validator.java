package scot.mygov.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Validator for model tenancy objects.
 */
public class Validator<T> {
    private static final Logger LOG = LoggerFactory.getLogger(Validator.class);

    private final List<ValidationRule<T>> rules;

    public Validator(List<ValidationRule<T>> rules) {
        this.rules = rules;
    }

    public void validate(T model) {
        ValidationResultsBuilder resultsBuilder = new ValidationResultsBuilder();
        for (ValidationRule<T> rule : rules) {
            rule.validate(model, resultsBuilder);
        }
        ValidationResults results = resultsBuilder.build();
        if (!results.getIssues().isEmpty()) {
            LOG.error("Failed validation: {}", results.getIssues());
            throw new ValidationException(results.getIssues());
        }
    }
}
