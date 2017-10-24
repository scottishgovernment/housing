package scot.mygov.housing.forms.modeltenancy;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;

public class RentPaymentFrequencyTest {

    @Test
    public void describeReturnsEmptyStringForNull() {
        Assert.assertEquals(RentPaymentFrequency.description(null), "");
    }

}
