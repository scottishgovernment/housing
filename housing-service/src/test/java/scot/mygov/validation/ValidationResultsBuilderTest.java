package scot.mygov.validation;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by z418868 on 19/06/2017.
 */
public class ValidationResultsBuilderTest {

    @Test
    public void canBuildResults() {
        ValidationResults results = new ValidationResultsBuilder().issue("one", "issueone").issue("two", "issuetwo").build();
        Assert.assertEquals(results.getIssues().get("one"), Collections.singletonList("issueone"));
        Assert.assertEquals(results.getIssues().get("two"), Collections.singletonList("issuetwo"));
    }

    @Test
    public void canBuildResultsWithMultipleIssuesForField() {
        ValidationResults results = new ValidationResultsBuilder().issue("one", "issueone").issue("one", "issuetwo").build();
        List<String> expectedIssues = new ArrayList<>();
        Collections.addAll(expectedIssues, "issueone", "issuetwo");
        Assert.assertEquals(expectedIssues, results.getIssues().get("one"));
    }

    @Test
    public void canClearIssues() {
        ValidationResults results = new ValidationResultsBuilder()
                .issue("one", "issue1")
                .issue("two", "issuetwo")
                .clear()
                .build();
        Assert.assertTrue(results.getIssues().isEmpty());

    }
}
