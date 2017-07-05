package scot.mygov.validation;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String, List<String>> issues;

    public ValidationException(Map<String, List<String>> issues) {
        super("Invalid Model tenancy");
        this.issues = issues;
    }

    public Map<String, List<String>> getIssues() {
        return issues;
    }


}
