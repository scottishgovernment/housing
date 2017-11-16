package scot.mygov.housing.forms.rentincreasenotice;

import com.aspose.words.DocumentBuilder;
import scot.mygov.documents.PlaceholderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static scot.mygov.documents.PlaceholderUtils.writeInlineField;
import static scot.mygov.documents.PlaceholderUtils.writeLines;

public class RentIncreasePlaceholders {

    public static Map<String, Consumer<DocumentBuilder>> placeholders() {
        Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();
        placeholders.put("tenantNames", documentBuilder -> writeLines(documentBuilder, 2));
        placeholders.put("tenantAddresses", documentBuilder -> writeLines(documentBuilder, 5));
        placeholders.put("landlordNames", documentBuilder -> writeLines(documentBuilder, 2));
        placeholders.put("landlordAddresses", documentBuilder -> writeLines(documentBuilder, 5));
        placeholders.put("landlordOrAgentName", documentBuilder -> writeLines(documentBuilder, 2));
        placeholders.put("landlordsAgentAddress", documentBuilder -> writeLines(documentBuilder, 5));
        placeholders.put("rentIncreaseDate", documentBuilder -> writeInlineField(documentBuilder, PlaceholderUtils.DATE_PLACEHOLDER));
        placeholders.put("lastRentIncreaseDate", documentBuilder -> writeInlineField(documentBuilder, PlaceholderUtils.DATE_PLACEHOLDER));
        placeholders.put("capFromDate", documentBuilder -> writeInlineField(documentBuilder, PlaceholderUtils.DATE_PLACEHOLDER));
        placeholders.put("capToDate", documentBuilder -> writeInlineField(documentBuilder, PlaceholderUtils.DATE_PLACEHOLDER));
        return placeholders;
    }
}
