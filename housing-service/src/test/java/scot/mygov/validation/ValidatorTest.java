package scot.mygov.validation;

import org.junit.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;

public class ValidatorTest {


    @Test(expected = ValidationException.class)
    public void throwsExceptionIfAnyIssues() throws ValidationException {

        Validator<Value> sut = new Validator<>(singletonList(new FailingRule()));
        try {
            sut.validate(new Value("bar"));
        } catch (ValidationException e) {
            assertTrue(!e.getIssues().isEmpty());
            throw e;
        }
    }

    @Test
    public void noExceptionIfNoIssues() throws ValidationException {
        Validator<Value> sut = new Validator<>(emptyList());
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
