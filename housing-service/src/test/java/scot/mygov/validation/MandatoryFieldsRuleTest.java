package scot.mygov.validation;


import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

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
        assertEquals(res.getIssues().get("foo"), singletonList("Invalid field"));
        assertEquals(res.getIssues().get("nullField"), singletonList("Required"));
        assertEquals(res.getIssues().get("emptyField"), singletonList("Required"));
        assertEquals(res.getIssues().get("validField"), null);
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
