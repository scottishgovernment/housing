package scot.mygov.validation;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.addAll;
import static org.junit.Assert.assertEquals;

public class MoneyFieldsRuleTest {

    @Test
    public void acceptsValidMonetaryValues() {
        // ARRANGE
        MoneyFieldsRule<Value> rule = new MoneyFieldsRule<>("money");
        List<String> inputs = new ArrayList<>();
        addAll(inputs, "10.00", "200.19", "1000000.00");

        // ACT
        long validCount = inputs.stream()
                .map(input -> new Value(input))
                .filter(input -> {
                    ValidationResultsBuilder b = new ValidationResultsBuilder();
                    rule.validate(input, b);
                    return b.build().getIssues().isEmpty();
                }).count();


        // ASSERT
        assertEquals(validCount, inputs.size());
    }

    @Test
    public void rejectsInvalidMonetaryValues() {
        // ARRANGE
        MoneyFieldsRule<Value> rule = new MoneyFieldsRule<>("money");
        List<String> inputs = new ArrayList<>();
        addAll(inputs, null, "", "ten", "10", "10.0", "100.000", "1000,000.00");

        // ACT
        long validCount = inputs.stream()
                .map(input -> new Value(input))
                .filter(input -> {
                    ValidationResultsBuilder b = new ValidationResultsBuilder();
                    rule.validate(input, b);
                    return b.build().getIssues().isEmpty();
                }).count();


        // ASSERT
        assertEquals(validCount, 0);
    }

    @Test
    public void rejectsUnknownProperty() {
        // ARRANGE
        MoneyFieldsRule<Value> rule = new MoneyFieldsRule<>("invalidPropety");

        // ACT
        ValidationResultsBuilder b = new ValidationResultsBuilder();
        rule.validate(new Value("100.00"), b);

        // ASSERT
        assertEquals(b.build().getIssues().size(), 1);
    }

    public class Value {
        private final String money;

        public Value(String money) {
            this.money = money;
        }

        public String getMoney() {
            return money;
        }
    }
}
