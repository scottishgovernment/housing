package scot.mygov.housing.forms;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.Person;

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
    public void defaultForEmpty() {
        Assert.assertEquals(FieldExtractorUtils.defaultForEmpty(null, "default"), "default");
    }

    @Test
    public void defaultForEmptyReturnValueWhenNotEmpty() {
        Assert.assertEquals(FieldExtractorUtils.defaultForNull("value", "default"), "value");
    }
    @Test
    public void adressPartsReturnEmptyListForNullAddress() {
        Assert.assertEquals(Collections.emptyList(), FieldExtractorUtils.addressParts(null));
    }

    @Test
    public void isEmptyPersonReturnsTrueForNull() {
        Assert.assertEquals(true, FieldExtractorUtils.isEmpty((Person) null));
    }

    @Test
    public void isEmptyPersonReturnsTrueForEmptyPerson() {
        Assert.assertEquals(true, FieldExtractorUtils.isEmpty(new Person()));
    }

    @Test
    public void isEmptyPersonReturnsFalseForNonEmptyPerson() {
        Person p = new Person();
        p.setName("hello");
        Assert.assertEquals(false, FieldExtractorUtils.isEmpty(p));
    }

    @Test
    public void isEmptyAddressReturnsTrueForNull() {
        Assert.assertEquals(true, FieldExtractorUtils.isEmpty((Address) null));
    }

    @Test
    public void isEmptyAddressReturnsTrueForEmptyPerson() {
        Assert.assertEquals(true, FieldExtractorUtils.isEmpty(new Address()));
    }

    @Test
    public void isEmptyAddressReturnsFalseForNonEmptyPerson() {
        Address a = new Address();
        a.setStreet("hello");
        Assert.assertEquals(false, FieldExtractorUtils.isEmpty(a));
    }
}
