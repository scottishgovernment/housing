package scot.mygov.housing.forms.rentadjudication;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;
import scot.mygov.housing.forms.rentadjudication.model.Room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static scot.mygov.housing.forms.FieldExtractorUtils.addressParts;
import static scot.mygov.housing.forms.FieldExtractorUtils.naForEmpty;
import static scot.mygov.housing.forms.FieldExtractorUtils.defaultForEmpty;

public class RentAdjudicationFieldExtractor implements FieldExtractor<RentAdjudication> {

    public Map<String, Object> extractFields(RentAdjudication model) {

        Map<String, Object> fields = new HashMap<>();

        // the order we extract the fields will not change the behaviour of this code, but for clarity we extract
        // them in the order they apear in the documents template.
        extractPeople("tenants", fields, model.getTenants());
        extractPeople("tenantsAgent", fields, singleton(model.getTenantAgent()));
        extractLandlords(fields, model.getLandlords());
        extractPeople("landlordsAgent", fields, singleton(model.getLettingAgent()));
        fields.put("propertyType", model.getPropertyType());
        extractRooms(model, fields);
        extractSharedAreas(model, fields);
        extractIncluded(model, fields);
        fields.put("heating", model.getHeating());
        fields.put("doubleGlazing", model.getDoubleGlazing());
        extractServices(model, fields);
        fields.put("furnished", isNotEmpty(model.getFurnished()) ? "Yes" : "No");
        extractImprovements(model, fields);
        extractDamages(model, fields);
        extractRentDetails(model, fields);
        fields.put("notAvailableForInspection", model.getNotAvailableForInspection());
        return fields;
    }

    private void extractPeople(String field,  Map<String, Object> fields, Collection<Person> people ) {
        fields.put(field,
                naForEmpty(people.stream().filter(Objects::nonNull).map(this::personString).collect(joining("\n\n")))
        );
    }

    private void extractLandlords(Map<String, Object> fields, Collection<AgentOrLandLord> landlords ) {
        fields.put("landlords",
                naForEmpty(landlords.stream().filter(Objects::nonNull).map(this::agentOrLandlordString).collect(joining("\n\n")))
        );
    }

    private String personString(Person person) {
        List<String> parts = new ArrayList<String>();
        parts.add(person.getName());
        parts.addAll(addressParts(person.getAddress()));
        parts.add(person.getEmail());
        parts.add(person.getTelephone());
        return parts.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining("\n"));
    }

    private String agentOrLandlordString(AgentOrLandLord agentOrLandLord) {
        String personString = personString(agentOrLandLord);
        if (isNotEmpty(agentOrLandLord.getRegistrationNumber())) {
            personString += "\n" + "Registration Number: " + agentOrLandLord.getRegistrationNumber();
        }
        return personString;
    }

    private void extractRooms(RentAdjudication model, Map<String, Object> fields) {
        String roomsString = model.getRooms()
                .stream()
                .map(this::roomString)
                .collect(joining("\n"));
        fields.put("rooms", roomsString);
    }

    private String roomString(Room room) {
        return String.format("%s (%s)", room.getName(), room.getQuantity());
    }

    private void extractSharedAreas(RentAdjudication model, Map<String, Object> fields) {
        fields.put("hasSharedAreas", isNotEmpty(model.getSharedAreas()) ? "Yes" : "No");
        fields.put("sharedAreas", defaultForEmpty(model.getSharedAreas(), ""));
    }

    private void extractIncluded(RentAdjudication model, Map<String, Object> fields) {
        fields.put("hasIncluded", isNotEmpty(model.getIncluded()) ? "Yes" : "No");
        fields.put("included", defaultForEmpty(model.getIncluded(), ""));
    }

    private void extractServices(RentAdjudication model, Map<String, Object> fields) {
        fields.put("hasServices", isNotEmpty(model.getServicesDetails()) ? "Yes" : "No");
        fields.put("servicesDetails", defaultForEmpty(model.getServicesDetails(), ""));
        fields.put("servicesCostDetails", defaultForEmpty(model.getServicesCostDetails(), ""));
    }

    private void extractImprovements(RentAdjudication model, Map<String, Object> fields) {
        fields.put("hasTenantImprovements", isNotEmpty(model.getImprovementsTenant()) ? "Yes" : "No");
        fields.put("tenantImprovements", defaultForEmpty(model.getImprovementsTenant(), ""));
        fields.put("hasLandlordImprovements", isNotEmpty(model.getImprovementsLandlord()) ? "Yes" : "No");
        fields.put("landlordImprovements", defaultForEmpty(model.getImprovementsLandlord(), ""));
    }

    private void extractDamages(RentAdjudication model, Map<String, Object> fields) {
        fields.put("hasDamages", isNotEmpty(model.getDamage()) ? "Yes" : "No");
        fields.put("damages", defaultForEmpty(model.getDamage(), ""));
    }

    private void extractRentDetails(RentAdjudication model, Map<String, Object> fields) {
        fields.put("currentRentAmount", model.getCurrentRentAmount());
        fields.put("currentRentFrequency", RentPaymentFrequency.description(model.getCurrentRentFrequency()));
        fields.put("newRentAmount", model.getNewRentAmount());
        fields.put("newRentFrequency", RentPaymentFrequency.description(model.getNewRentFrequency()));
    }
}
