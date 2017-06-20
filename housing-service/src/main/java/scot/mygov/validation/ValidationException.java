package scot.mygov.validation;

import java.util.List;
import java.util.Map;

/**
 * Created by z418868 on 15/06/2017.
 */
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
