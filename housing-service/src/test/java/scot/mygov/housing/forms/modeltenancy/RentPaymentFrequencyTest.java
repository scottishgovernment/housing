package scot.mygov.housing.forms.modeltenancy;

import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;

import static org.junit.Assert.assertEquals;

public class RentPaymentFrequencyTest {

    @Test
    public void describeReturnsEmptyStringForNull() {
        assertEquals("", RentPaymentFrequency.description(null));
    }

}
