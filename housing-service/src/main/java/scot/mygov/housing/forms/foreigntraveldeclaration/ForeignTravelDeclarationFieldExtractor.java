package scot.mygov.housing.forms.foreigntraveldeclaration;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.TravelReason;

import java.util.HashMap;
import java.util.Map;

import static scot.mygov.housing.forms.FieldExtractorUtils.formatDate;

public class ForeignTravelDeclarationFieldExtractor implements FieldExtractor<ForeignTravelDeclaration> {

    public Map<String, Object> extractFields(ForeignTravelDeclaration model) {

        Map<String, Object> fields = new HashMap<>();

        fields.put("title", model.getTitle());
        fields.put("name", model.getName());
        fields.put("surname", model.getSurname());
        if (model.getDob() != null) {
            fields.put("dob", formatDate(model.getDob()));
        }
        fields.put("nationality", model.getNationality());
        fields.put("address", model.getAddress());
        fields.put("travelOperator", model.getTravelOperator());
        fields.put("namesOfMinors", model.getNamesOfMinors());
        fields.put("furnishingType", TravelReason.describe(model.getReason()));
        fields.put("otherReason", model.getOtherReason());
        fields.put("signature", model.getSignature());
        if (model.getDob() != null) {
            fields.put("signedDate", formatDate(model.getSignedDate()));
        }
        return fields;
    }

}