package scot.mygov.housing.forms.nonprovisionofdocumentation;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.FieldExtractorUtils;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;
import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;
import static scot.mygov.housing.forms.FieldExtractorUtils.formatDate;

public class NonProvisionOfDocumentationFieldExtractor implements FieldExtractor<NonProvisionOfDocumentation> {

    public static final String CHECKED = "_X_";
    public static final String UNCHECKED = "___";

    public Map<String, Object> extractFields(NonProvisionOfDocumentation model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("landlordsOrAgentNamesAndAddresses", landlordsOrAgentNamesAndAddresses(model));
        fields.put("tenantNames", model.getTenantNames().stream().collect(joining(", ")));
        fields.put("address", addressFieldsMultipleLines(model.getAddress()));
        fields.put("intendedReferralDate", formatDate(model.getIntendedReferralDate()));

        if (model.isSection10Failure()) {
            fields.put("section10Failure", CHECKED);
            fields.put("section10Details", model.getSection10Details());
        } else {
            fields.put("section10Failure", UNCHECKED);
            fields.put("section10Details", "");
        }

        if (model.isSection11Failure()) {
            fields.put("section11Failure", CHECKED);
            fields.put("section11Details", model.getSection11Details());
        } else {
            fields.put("section11Failure", UNCHECKED);
            fields.put("section11Details", "");
        }

        if (model.isSection16Failure()) {
            fields.put("section16Failure", CHECKED);
        } else {
            fields.put("section16Failure", UNCHECKED);
        }

        fields.put("tenantAgentName", model.getTenantsAgent().getName());
        fields.put("tenantAgentAddress", addressFieldsMultipleLines(model.getTenantsAgent().getAddress()));

        return fields;
    }

    private String landlordsOrAgentNamesAndAddresses(NonProvisionOfDocumentation model) {
        List<AgentOrLandLord> all = new ArrayList<>(model.getLandlords());
        all.add(model.getLandlordsAgent());
        return all.stream()
                .filter(Objects::nonNull)
                .filter(FieldExtractorUtils::isNotEmpty)
                .map(FieldExtractorUtils::nameAndAddressFieldsMultipleLines)
                .collect(joining("\n\n"));
    }
}
