package scot.mygov.housing.forms.noticetoleave;

import com.aspose.words.DocumentBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static scot.mygov.documents.PlaceholderUtils.inlineDate;
import static scot.mygov.documents.PlaceholderUtils.lines;

public class NoticeToLeavePlaceholders {

    private NoticeToLeavePlaceholders() {
        // do not instantiate
    }

    public static Map<String, Consumer<DocumentBuilder>> placeholders() {
        Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();
        placeholders.put("tenantNames", lines(1));
        placeholders.put("address", lines(5));
        placeholders.put("reasonDetails", lines(6));
        placeholders.put("supportingEvidence", lines(6));
        placeholders.put("earliestTribunualDate", inlineDate());
        return placeholders;
    }

}
