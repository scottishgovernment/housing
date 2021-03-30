package scot.mygov.housing.forms.foreigntraveldeclaration;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.TravelReason;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;
import static scot.mygov.housing.forms.FieldExtractorUtils.defaultForEmpty;

public class ForeignTravelDeclarationFieldExtractor implements FieldExtractor<ForeignTravelDeclaration> {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Map<String, Object> extractFields(ForeignTravelDeclaration model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", model.getName());
        fields.put("address", addressFieldsMultipleLines(model.getAddress()));
        fields.put("dob", formatDate(model.getDob()));
        fields.put("nationality", model.getNationality());
        fields.put("passportNumber", model.getPassportNumber());
        addTravelReasons(fields, model.getReason());
        fields.put("reason", model.getReason());
        fields.put("otherReason", defaultForEmpty(model.getOtherReason(), "n/a"));
        fields.put("signature", model.getSignature());
        fields.put("signedDate", formatDate(model.getSignedDate()));
        return fields;
    }

    public static String formatDate(LocalDate date) {
        return DATE_FORMATTER.format(date);
    }

    private void addTravelReasons(Map<String, Object> fields, String reason) {
        for (TravelReason travelReason : TravelReason.values()) {
            fields.put(travelReason.name(), "_");
        }
        fields.put(reason, "X");
    }
}