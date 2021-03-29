package scot.mygov.housing.forms.foreigntraveldeclaration;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.TravelReason;

import java.util.HashMap;
import java.util.Map;

import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;
import static scot.mygov.housing.forms.FieldExtractorUtils.defaultForEmpty;
import static scot.mygov.housing.forms.FieldExtractorUtils.formatDate;

public class ForeignTravelDeclarationFieldExtractor implements FieldExtractor<ForeignTravelDeclaration> {

    public Map<String, Object> extractFields(ForeignTravelDeclaration model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", model.getName());
        fields.put("address", addressFieldsMultipleLines(model.getAddress()));
        fields.put("dob", formatDate(model.getDob(), ""));
        fields.put("nationality", model.getNationality());
        fields.put("passportNumber", model.getPassportNumber());
        addTravelReasons(fields, model.getReason());
        fields.put("reason", model.getReason());
        fields.put("otherReason", defaultForEmpty(model.getOtherReason(), "n/a"));
        fields.put("signature", model.getSignature());
        fields.put("signedDate", formatDate(model.getSignedDate(), ""));
        return fields;
    }

    private void addTravelReasons(Map<String, Object> fields, String reason) {
        for (TravelReason travelReason : TravelReason.values()) {
            fields.put(travelReason.name(), "_");
        }
        fields.put(reason, "X");
    }
}