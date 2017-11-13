package scot.mygov.housing.forms.nonprovisionofdocumentation;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;

public class NonProvisionOfDocumentationFieldExtractor implements FieldExtractor<NonProvisionOfDocumentation> {

    public Map<String, Object> extractFields(NonProvisionOfDocumentation model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("nameOfLandlordOrAgent", model.getLandLordOrAgent().getName());
        fields.put("addressOfLandlordOrAgent", addressFieldsMultipleLines(model.getLandLordOrAgent().getAddress()));
        fields.put("tenantNames", model.getTenantNames().stream().collect(joining(", ")));
        fields.put("address", addressFieldsMultipleLines(model.getAddress()));
        fields.put("intendedReferralDate", model.getIntendedReferalDate());

        if (model.isSection10Failure()) {
            fields.put("section10Failure", "X");
            fields.put("section10Details", model.getSection10Details());
        } else {
            fields.put("section10Failure", "_");
            fields.put("section10Details", "");
        }

        if (model.isSection11Failure()) {
            fields.put("section11Failure", "X");
            fields.put("section11Details", model.getSection11Details());
        } else {
            fields.put("section11Failure", "_");
            fields.put("section11Details", "");
        }

        if (model.isSection16Failure()) {
            fields.put("section16Failure", "X");
        } else {
            fields.put("section16Failure", "_");
        }

        fields.put("tenantAgentName", model.getTenantsAgent().getName());
        fields.put("tenantAgentAddress", addressFieldsMultipleLines(model.getTenantsAgent().getAddress()));

        return fields;
    }
}
