package scot.mygov.housing.forms.rentincreasenotice;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.FieldExtractorUtils;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.housing.forms.rentincreasenotice.model.Calculation;
import scot.mygov.housing.forms.rentincreasenotice.model.RentIncrease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;
import static scot.mygov.housing.forms.FieldExtractorUtils.formatDate;
import static scot.mygov.housing.forms.FieldExtractorUtils.peopleNames;

public class RentIncreaseFieldExtractor implements FieldExtractor<RentIncrease> {

    public Map<String, Object> extractFields(RentIncrease model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("tenantNames", model.getTenantNames().stream().collect(joining(", ")));
        fields.put("address", addressFieldsMultipleLines(model.getAddress()));
        fields.put("landlordNames", peopleNames(model.getLandlords()));
        fields.put("landlordAddresses", peopleAddresses(model.getLandlords()));
        fields.put("landlordOrAgentName", peopleNames(model.getLandlords()));
        if (model.getLandlordsAgent() != null) {
            fields.put("landlordsAgentAddress", addressFieldsMultipleLines(model.getLandlordsAgent().getAddress()));
        } else {
            fields.put("landlordsAgentAddress", "n/a");
        }
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

        fields.put("rentIncreaseDate", formatDate(model.getRentIncreaseDate()));
        fields.put("lastRentIncreaseDate", formatDate(model.getLastRentIncreaseDate()));
        fields.put("capFromDate", formatDate(model.getCapFromDate()));
        fields.put("capToDate", formatDate(model.getCapToDate()));
        fields.put("notificationDate", formatDate(model.getNotificationDate()));

        fields.put("oldRentAmount", model.getOldRentAmount());
        fields.put("oldRentPeriod", RentPaymentFrequency.description(model.getOldRentPeriod()));
        fields.put("newRentAmount", model.getNewRentAmount());
        fields.put("newRentPeriod", RentPaymentFrequency.description(model.getNewRentPeriod()));

        extractCalculation(fields, model.getCalculation());

        return fields;
    }

    private void extractCalculation(Map<String, Object> fields, Calculation calculation) {
        fields.put("calcCpi", calculation.getCpi());
        fields.put("calcX", calculation.getX());
        fields.put("calcY", calculation.getY());
    }

    private <T extends Person> String peopleAddresses(List<T> people) {
        return people.stream()
                .map(Person::getAddress)
                .map(FieldExtractorUtils::addressFieldsMultipleLines)
                .collect(joining("\n\n"));
    }

}
