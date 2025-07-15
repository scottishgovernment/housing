package scot.mygov.validation;

import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MandatoryFieldsRuleTest {


    @Test
    public void validatesAsExpected() {

        // ARRANGE
        Value value = new Value(null, "", "valid");
        MandatoryFieldsRule<Value> rule = new MandatoryFieldsRule<>("foo", "nullField", "emptyField", "validField");
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(value, b);
        ValidationResults res = b.build();

        // ASSERT
        assertEquals(singletonList("Invalid field"), res.getIssues().get("foo"));
        assertEquals(singletonList("Required"), res.getIssues().get("nullField"));
        assertEquals(singletonList("Required"), res.getIssues().get("emptyField"));
        assertNull(res.getIssues().get("validField"));
    }

    public static class Value {
        private final String nullField;
        private final String emptyField;
        private final String validField;

        public Value(String nullField, String emptyField, String validField) {
            this.nullField = nullField;
            this.emptyField = emptyField;
            this.validField = validField;
        }

        public String getNullField() {
            return nullField;
        }

        public String getEmptyField() {
            return emptyField;
        }

        public String getValidField() {
            return validField;
        }
    }
}
