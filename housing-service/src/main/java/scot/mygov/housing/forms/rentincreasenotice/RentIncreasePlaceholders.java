package scot.mygov.housing.forms.rentincreasenotice;

import com.aspose.words.DocumentBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static scot.mygov.documents.PlaceholderUtils.inlineDate;
import static scot.mygov.documents.PlaceholderUtils.lines;
import static scot.mygov.documents.PlaceholderUtils.put;

public class RentIncreasePlaceholders {

    private RentIncreasePlaceholders() {
        // do not instantiate
    }

    public static Map<String, Consumer<DocumentBuilder>> placeholders() {
        Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();
        put(placeholders, lines(2),
                "tenantNames", "landlordNames", "landlordOrAgentName");
        put(placeholders, lines(5),
                "tenantAddresses", "landlordAddresses", "landlordsAgentAddress");
        put(placeholders, inlineDate(),
                "rentIncreaseDate", "lastRentIncreaseDate", "capFromDate", "capToDate");
        return placeholders;
    }
}
