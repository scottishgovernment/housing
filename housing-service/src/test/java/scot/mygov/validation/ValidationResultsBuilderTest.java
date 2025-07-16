package scot.mygov.validation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ValidationResultsBuilderTest {

    @Test
    public void canBuildResults() {
        ValidationResults results = new ValidationResultsBuilder().issue("one", "issueone").issue("two", "issuetwo").build();
        assertEquals(results.getIssues().get("one"), singletonList("issueone"));
        assertEquals(results.getIssues().get("two"), singletonList("issuetwo"));
    }

    @Test
    public void canBuildResultsWithMultipleIssuesForField() {
        ValidationResults results = new ValidationResultsBuilder().issue("one", "issueone").issue("one", "issuetwo").build();
        List<String> expectedIssues = new ArrayList<>();
        addAll(expectedIssues, "issueone", "issuetwo");
        assertEquals(expectedIssues, results.getIssues().get("one"));
    }

    @Test
    public void canClearIssues() {
        ValidationResults results = new ValidationResultsBuilder()
                .issue("one", "issue1")
                .issue("two", "issuetwo")
                .clear()
                .build();
        assertTrue(results.getIssues().isEmpty());

    }
}
