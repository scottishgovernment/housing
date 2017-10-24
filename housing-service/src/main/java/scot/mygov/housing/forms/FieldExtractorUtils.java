package scot.mygov.housing.forms;

import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.addAll;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class FieldExtractorUtils {

    public static final String NOT_APPLICABLE = "n/a";

    private FieldExtractorUtils() {
        // prevent instantiation
    }

    public static List<String> addressParts(Address address) {
        if (address == null) {
            return Collections.emptyList();
        }

        List<String> parts = new ArrayList<>();
        addAll(parts,
                address.getBuilding(),
                address.getStreet(),
                address.getRegion(),
                address.getTown(),
                address.getPostcode());
        return parts.stream().filter(part -> isNotEmpty(part)).collect(toList());
    }

    public static String addressFieldsMultipleLines(Address address) {
        return addressParts(address).stream().collect(joining(",\n"));
    }

    public static String addressFieldsSingleLine(Address address) {
        return addressParts(address).stream().collect(joining(", "));
    }

    public static String nameAndAddress(Person person, int i) {
        String address = addressFieldsSingleLine(person.getAddress());
        String nameAndAddress = format("%s, %s", person.getName(), address);
        return numberedValue(nameAndAddress, i);
    }

    public static String numberedValue(String val, int i) {
        return format("(%d) %s", i, naForEmpty(val));
    }

    public static String naForEmpty(String value) {
        return defaultForEmpty(value, NOT_APPLICABLE);
    }

    public static String defaultForEmpty(String value, String defaultValue) {
        if (isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public static Object defaultForNull(Object value, String defaultValue) {
        if (isNull(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }
}
