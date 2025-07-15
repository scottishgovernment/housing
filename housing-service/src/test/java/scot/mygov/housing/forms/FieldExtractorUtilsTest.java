package scot.mygov.housing.forms;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.util.Collections;

import static org.junit.Assert.*;

public class FieldExtractorUtilsTest {
    @Test
    public void defaultForNull() {
        assertEquals("default", FieldExtractorUtils.defaultForNull(null, "default"));
    }

    @Test
    public void defaultForNullReturnValueWhenNotNull() {
        assertEquals("value", FieldExtractorUtils.defaultForNull("value", "default"));
    }

    @Test
    public void defaultForEmpty() {
        assertEquals("default", FieldExtractorUtils.defaultForEmpty(null, "default"));
    }

    @Test
    public void defaultForEmptyReturnValueWhenNotEmpty() {
        assertEquals("value", FieldExtractorUtils.defaultForNull("value", "default"));
    }
    @Test
    public void addressPartsReturnEmptyListForNullAddress() {
        assertEquals(Collections.emptyList(), FieldExtractorUtils.addressParts(null));
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
