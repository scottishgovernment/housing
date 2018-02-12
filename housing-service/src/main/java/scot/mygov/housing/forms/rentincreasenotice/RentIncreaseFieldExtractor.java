package scot.mygov.housing.forms.rentincreasenotice;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.FieldExtractorUtils;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.housing.forms.rentincreasenotice.model.RentIncrease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;
import static scot.mygov.housing.forms.FieldExtractorUtils.peopleNames;

public class RentIncreaseFieldExtractor implements FieldExtractor<RentIncrease> {

    public Map<String, Object> extractFields(RentIncrease model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("tenantNames", model.getTenantNames().stream().collect(joining(", ")));
        fields.put("address", addressFieldsMultipleLines(model.getAddress()));
        fields.put("landlordNames", peopleNames(model.getLandlords()));
        fields.put("landlordAddresses", peopleAddresses(model.getLandlords()));
        fields.put("landlordOrAgentName", peopleNames(model.getLandlords()));

        String inRentPressureZoneCheckbox = "_";
        String notInRentPressureZoneCheckbox = "_";
        if ("true".equals(model.getInRentPressureZone())) {
            inRentPressureZoneCheckbox = "X";
        }
        if ("false".equals(model.getInRentPressureZone())) {
            notInRentPressureZoneCheckbox = "X";
        }
        fields.put("inRentPressureZoneCheckbox", inRentPressureZoneCheckbox);
        fields.put("notInRentPressureZoneCheckbox", notInRentPressureZoneCheckbox);

        fields.put("rentIncreaseDate", model.getRentIncreaseDate());
        fields.put("lastRentIncreaseDate", model.getLastRentIncreaseDate());
        fields.put("capFromDate", model.getCapFromDate());
        fields.put("capToDate", model.getCapToDate());

        fields.put("oldRentAmount", model.getOldRentAmount());
        fields.put("oldRentPeriod", RentPaymentFrequency.description(model.getOldRentPeriod()));
        fields.put("newRentAmount", model.getNewRentAmount());
        fields.put("newRentPeriod", RentPaymentFrequency.description(model.getNewRentPeriod()));

        return fields;
    }

    private <T extends Person> String peopleAddresses(List<T> people) {
        return people.stream()
                .map(Person::getAddress)
                .map(FieldExtractorUtils::addressFieldsMultipleLines)
                .collect(joining("\n\n"));
    }

}
