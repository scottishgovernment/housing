package scot.mygov.housing.forms.foreigntraveldeclaration;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;

import java.util.HashMap;
import java.util.Map;

import static scot.mygov.housing.forms.FieldExtractorUtils.formatDate;

public class ForeignTravelDeclarationFieldExtractor implements FieldExtractor<ForeignTravelDeclaration> {

    public Map<String, Object> extractFields(ForeignTravelDeclaration model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("title", model.getTitle());
        fields.put("name", model.getName());
        fields.put("surname", model.getSurname());
        fields.put("dob", formatDate(model.getDob(), ""));
        fields.put("nationality", model.getNationality());
        fields.put("address", model.getAddress());
        fields.put("travelOperator", model.getTravelOperator());
        fields.put("namesOfMinors", model.getNamesOfMinors());
        fields.put("reason", model.getReason());
        fields.put("personCompletingForm", model.getPersonCompletingForm());
        fields.put("signature", model.getSignature());
        fields.put("signedDate", formatDate(model.getSignedDate(), ""));
        return fields;
    }

}