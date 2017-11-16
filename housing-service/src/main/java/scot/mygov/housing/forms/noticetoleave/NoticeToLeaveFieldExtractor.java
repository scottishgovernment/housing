package scot.mygov.housing.forms.noticetoleave;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.FieldExtractorUtils;
import scot.mygov.housing.forms.noticetoleave.model.NoticeToLeave;
import scot.mygov.housing.forms.noticetoleave.model.Reason;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class NoticeToLeaveFieldExtractor implements FieldExtractor<NoticeToLeave> {

    public Map<String, Object> extractFields(NoticeToLeave model) {

        Map<String, Object> fields = new HashMap<>();
        fields.put("tenantNames", model.getTenantNames().stream().collect(joining(", ")));
        fields.put("address", FieldExtractorUtils.addressFieldsMultipleLines(model.getAddress()));
        extractReasons(fields, model);
        fields.put("reasonDetails", model.getReasonDetails());
        fields.put("supportingEvidence", model.getSupportingEvidence());
        fields.put("earliestTribunualDate", model.getEarliestTribunualDate());
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
}
