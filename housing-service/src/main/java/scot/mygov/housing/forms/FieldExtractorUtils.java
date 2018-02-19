package scot.mygov.housing.forms;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.addAll;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class FieldExtractorUtils {

    public static final String NOT_APPLICABLE = "n/a";

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private FieldExtractorUtils() {
        // prevent instantiation
    }

    public static String naForEmpty(String value) {
        return defaultForEmpty(value, NOT_APPLICABLE);
    }

    public static String defaultForEmpty(String value, String defaultValue) {
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public static String nameAndAddressFieldsMultipleLines(Person person) {
        List<String> lines = new ArrayList<>();
        lines.add(person.getName());
        lines.add("\n");
        lines.add("At:");
        lines.addAll(addressParts(person.getAddress()).stream().map(part -> part + ",").collect(toList()));
        return lines.stream().collect(joining("\n"));
    }


    public static String addressFieldsMultipleLines(Address address) {
        return addressParts(address).stream().collect(joining(",\n"));
    }

    public static String nameAndAddressMultipleLines(Person person, int i) {
        String address = addressFieldsMultipleLines(person.getAddress());
        String nameAndAddress = format("%s, %s", person.getName(), address);
        return numberedValue(nameAndAddress, i);
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

    public static Object defaultForNull(Object value, String defaultValue) {
        if (isNull(value)) {
            return defaultValue;
        } else {
            return value;
        }
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
        return parts.stream().filter(part -> StringUtils.isNotEmpty(part)).collect(toList());
    }

    public static <T extends Person> String peopleNames(List<T> people) {
        return people.stream().map(Person::getName).collect(joining(", "));
    }

    public static boolean isEmpty(Person person) {

        return person == null || (allEmpty(person.getName(), person.getTelephone(), person.getEmail()) &&
                isEmpty(person.getAddress()));
    }

    public static boolean isNotEmpty(Person person) {
        return !isEmpty(person);
    }

    public static boolean isEmpty(Address address) {
        return address == null ||
                allEmpty(
                    address.getBuilding(),
                    address.getPostcode(),
                    address.getRegion(),
                    address.getStreet(),
                    address.getTown());
    }

    public static boolean allEmpty(String ...values) {
        for (String value : values) {
            if (!StringUtils.isEmpty(value)) {
                return false;
            }
        }
        return true;
    }

    public static String formatDate(LocalDate date) {
        return formatDate(date, "");
    }

    public static String formatDate(LocalDate date, String defaultString) {
        if (date == null) {
            return defaultString;
        }
        return DATE_FORMATTER.format(date);
    }

    public static void extractLandlords(Map<String, Object> fields, Collection<AgentOrLandLord> landlords ) {
        fields.put("landlords",
                naForEmpty(landlords.stream()
                        .filter(Objects::nonNull)
                        .map(FieldExtractorUtils::agentOrLandlordString)
                        .collect(joining("\n\n")))
        );
    }

    public static String personString(Person person) {
        List<String> parts = new ArrayList<String>();
        parts.add(person.getName());
        parts.addAll(addressParts(person.getAddress()));
        parts.add(person.getEmail());
        parts.add(person.getTelephone());
        return parts.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining("\n"));
    }

    public static String agentOrLandlordString(AgentOrLandLord agentOrLandLord) {
        String personString = personString(agentOrLandLord);
        if (StringUtils.isNotEmpty(agentOrLandLord.getRegistrationNumber())) {
            personString += "\n" + "Registration Number: " + agentOrLandLord.getRegistrationNumber();
        }
        return personString;
    }

    public static void extractPeople(String field,  Map<String, Object> fields, Collection<Person> people ) {
        fields.put(field,
                naForEmpty(people.stream().filter(Objects::nonNull).map(FieldExtractorUtils::personString).collect(joining("\n\n")))
        );
    }
}
