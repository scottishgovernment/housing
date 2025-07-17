package scot.mygov.housing.forms;

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
        assertTrue(FieldExtractorUtils.isEmpty((Person) null));
    }

    @Test
    public void isEmptyPersonReturnsTrueForEmptyPerson() {
        assertTrue(FieldExtractorUtils.isEmpty(new Person()));
    }

    @Test
    public void isEmptyPersonReturnsFalseForNonEmptyPerson() {
        Person p = new Person();
        p.setName("hello");
        assertFalse(FieldExtractorUtils.isEmpty(p));
    }

    @Test
    public void isEmptyAddressReturnsTrueForNull() {
        assertTrue(FieldExtractorUtils.isEmpty((Address) null));
    }

    @Test
    public void isEmptyAddressReturnsTrueForEmptyPerson() {
        assertTrue(FieldExtractorUtils.isEmpty(new Address()));
    }

    @Test
    public void isEmptyAddressReturnsFalseForNonEmptyPerson() {
        Address a = new Address();
        a.setStreet("hello");
        assertFalse(FieldExtractorUtils.isEmpty(a));
    }
}
