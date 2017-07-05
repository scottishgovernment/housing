package scot.mygov.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationResultsBuilder {
    private final Map<String, List<String>> issues = new HashMap<>();

    public ValidationResultsBuilder issue(String field, String issue) {
        if (!issues.containsKey(field)) {
            issues.put(field, new ArrayList<>());
        }
        issues.get(field).add(issue);
        return this;
    }

    public ValidationResultsBuilder clear() {
        issues.clear();
        return this;
    }

    public ValidationResults build() {
        return new ValidationResults(issues);
    }
}