package scot.mygov.housing.forms;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class FieldExtractorUtilsTest {
    @Test
    public void defaultForNull() {
        Assert.assertEquals(FieldExtractorUtils.defaultForNull(null, "default"), "default");
    }

    @Test
    public void defaultForNullReturnValueWhenNotNull() {
        Assert.assertEquals(FieldExtractorUtils.defaultForNull("value", "default"), "value");
    }

    @Test
    public void adressPartsReturnEmptyListForNullAddress() {
        Assert.assertEquals(Collections.emptyList(), FieldExtractorUtils.addressParts(null));
    }
}
