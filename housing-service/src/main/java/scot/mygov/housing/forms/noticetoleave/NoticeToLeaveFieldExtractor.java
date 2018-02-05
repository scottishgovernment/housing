package scot.mygov.housing.forms.noticetoleave;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.noticetoleave.model.NoticeToLeave;
import scot.mygov.housing.forms.noticetoleave.model.Reason;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;
import static scot.mygov.housing.forms.FieldExtractorUtils.formatDate;
import static scot.mygov.housing.forms.FieldExtractorUtils.naForEmpty;

public class NoticeToLeaveFieldExtractor implements FieldExtractor<NoticeToLeave> {

    private static String I_OR_WE_FIELD = "iOrWe";
    private static String I_OR_WE_INLINE_FIELD = "iOrWeInline";
    private static String ARE_OR_AM_FIELD = "areOrAm";
    private static String LANDLORDS_OR_AGENT_DESCRIPTION_FIELD = "landlordsOrAgentDescrption";
    private static String LANDLORDS_OR_AGENT_NAMES_AND_ADDRESSES_FIELD = "landlordsOrAgentNamesAndAddresses";

    public Map<String, Object> extractFields(NoticeToLeave model) {

        Map<String, Object> fields = new HashMap<>();
        describeLandlordsOrAgent(model, fields);
        fields.put("tenantNames", model.getTenantNames().stream().collect(joining(", ")));
        fields.put("address", addressFieldsMultipleLines(model.getAddress()));
        fields.put("entryDate", formatDate(model.getEntryDate()));
        extractReasons(fields, model);
        fields.put("reasonDetails", naForEmpty(model.getReasonDetails()));
        fields.put("supportingEvidence", naForEmpty(model.getSupportingEvidence()));
        fields.put("earliestTribunualDate", formatDate(model.getEarliestTribunalDate()));
        return fields;
    }

    private void extractReasons(Map<String, Object> fields, NoticeToLeave model) {
        for (Reason reason : Reason.values()) {
            fields.put(reason.name(), "_");
        }
        for (String reason : model.getReasons()) {
            fields.put(reason, "X");
        }
    }

    private void describeLandlordsOrAgent(NoticeToLeave model, Map<String, Object> fields) {
        if (model.getLandlordsAgent() != null) {
            fields.put(I_OR_WE_FIELD, "I");
            fields.put(I_OR_WE_INLINE_FIELD, "I");
            fields.put(ARE_OR_AM_FIELD, "am");
            fields.put(LANDLORDS_OR_AGENT_DESCRIPTION_FIELD, "Landlord's Agent");
            fields.put(LANDLORDS_OR_AGENT_NAMES_AND_ADDRESSES_FIELD, formatNamesAndAddress(model.getLandlordsAgent()));
            return;
        }

        fields.put("landlordsOrAgentNamesAndAddresses",
                model.getLandlords().stream()
                        .map(this::formatNamesAndAddress)
                        .collect(joining("\n\n")));

        if (model.getLandlords().size() > 1) {
            fields.put(I_OR_WE_FIELD, "We");
            fields.put(I_OR_WE_INLINE_FIELD, "we");
            fields.put(ARE_OR_AM_FIELD, "are");
            fields.put(LANDLORDS_OR_AGENT_DESCRIPTION_FIELD, "Landlords");
            return;
        }

        fields.put(I_OR_WE_FIELD, "I");
        fields.put(ARE_OR_AM_FIELD, "am");
        fields.put(I_OR_WE_INLINE_FIELD, "I");
        fields.put(LANDLORDS_OR_AGENT_DESCRIPTION_FIELD, "Landlord");
    }

    private String formatNamesAndAddress(AgentOrLandLord person) {
        return String.format("%s Of\n%s", person.getName(), addressFieldsMultipleLines(person.getAddress()));
    }

}
