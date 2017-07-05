package scot.mygov.validation;

import java.util.List;
import java.util.Map;

public class ValidationResults {

    private final Map<String, List<String>> issues;

    public ValidationResults(Map<String, List<String>> issues) {
        this.issues = issues;
    }

    public Map<String, List<String>> getIssues() {
        return issues;
    }

}
