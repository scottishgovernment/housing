package scot.mygov.housing.forms.nonprovisionofdocumentation;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static scot.mygov.documents.PlaceholderUtils.DATE_PLACEHOLDER;
import static scot.mygov.documents.PlaceholderUtils.writeInlineField;
import static scot.mygov.documents.PlaceholderUtils.writeLines;

public class NonProvisionOfDocumentationPlaceholders {

    public static Map<String, Consumer<DocumentBuilder>> placeholders() {
        Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();
        placeholders.put("nameOfLandlordOrAgent", documentBuilder -> writeLines(documentBuilder, 1));
        placeholders.put("addressOfLandlordOrAgent", documentBuilder -> writeLines(documentBuilder, 5));
        placeholders.put("tenantNames", documentBuilder -> writeLines(documentBuilder, 4));
        placeholders.put("address", documentBuilder -> writeLines(documentBuilder, 5));
        placeholders.put("intendedReferralDate", documentBuilder -> writeInlineField(documentBuilder, DATE_PLACEHOLDER));
        placeholders.put("tenantAgentAddress", documentBuilder -> writeLines(documentBuilder, 5));
        placeholders.put("section10Details", documentBuilder -> writeLines(documentBuilder, 1));
        placeholders.put("section11Details", documentBuilder -> writeLines(documentBuilder, 1));
        return placeholders;
    }

}
