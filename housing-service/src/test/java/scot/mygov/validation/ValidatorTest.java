package scot.mygov.validation;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by z418868 on 19/06/2017.
 */
public class ValidatorTest {


    @Test(expected = ValidationException.class)
    public void throwsExceptionIfAnyIssues() throws ValidationException {

        Validator<Value> sut = new Validator<>(Collections.singletonList(new FailingRule()));
        try {
            sut.validate(new Value("bar"));
        } catch (ValidationException e) {
            Assert.assertTrue(!e.getIssues().isEmpty());
            throw e;
        }
    }

    @Test
    public void noExceptionIfNoIssues() throws ValidationException {
        Validator<Value> sut = new Validator<>(Collections.emptyList());
        sut.validate(new Value("bar"));
    }

    private class FailingRule implements ValidationRule<Value> {
        public void validate(Value model, ValidationResultsBuilder resultsBuilder) {
            resultsBuilder.issue("foo", "always broken");
        }
    }

    private class Value {
        private String foo;

        public Value(String foo) {
            this.foo = foo;
        }

        public String getFoo() {
            return foo;
        }
    }
}