package scot.mygov.validation;


import org.junit.Assert;
import org.junit.Test;
import scot.mygov.validation.MandatoryFieldsRule;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

import java.util.Collections;

/**
 * Created by z418868 on 19/06/2017.
 */
public class MandatoryFieldsRuleTest {


    @Test
    public void validatesAsExpected() {

        // ARRANGE
        Value value = new Value("value", "", null);
        MandatoryFieldsRule<Value> rule = new MandatoryFieldsRule<>("null", "", "foo", "field1", "field2", "field3");
        ValidationResultsBuilder b = new ValidationResultsBuilder();

        // ACT
        rule.validate(value, b);
        ValidationResults res = b.build();

        // ASSERT
        Assert.assertEquals(res.getIssues().get("foo"), null);
        Assert.assertEquals(res.getIssues().get("field1"), null);
        Assert.assertEquals(res.getIssues().get("field2"), Collections.singletonList("Required"));
        Assert.assertEquals(res.getIssues().get("field3"), Collections.singletonList("Required"));
    }

    public static class Value {
        private final String field1;
        private final String field2;
        private final String field3;

        public Value(String field1, String field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }

        public String getFoo() {
            return "foo";
        }
        public String getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }

        public String getField3() {
            return field3;
        }
    }
}
