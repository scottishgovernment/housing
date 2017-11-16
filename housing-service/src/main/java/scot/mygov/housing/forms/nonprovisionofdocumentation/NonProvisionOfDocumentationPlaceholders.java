package scot.mygov.housing.forms.nonprovisionofdocumentation;

import com.aspose.words.DocumentBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static scot.mygov.documents.PlaceholderUtils.inlineDate;
import static scot.mygov.documents.PlaceholderUtils.lines;
import static scot.mygov.documents.PlaceholderUtils.put;


public class NonProvisionOfDocumentationPlaceholders {

    private NonProvisionOfDocumentationPlaceholders() {
        // do not instantiate
    }

    public static Map<String, Consumer<DocumentBuilder>> placeholders() {
        Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();
        put(placeholders, lines(1), "nameOfLandlordOrAgent", "section10Details", "section11Details");
        put(placeholders, lines(5), "addressOfLandlordOrAgent", "tenantNames", "address", "tenantAgentAddress");
        placeholders.put("intendedReferralDate", inlineDate());
        return placeholders;
    }

}
