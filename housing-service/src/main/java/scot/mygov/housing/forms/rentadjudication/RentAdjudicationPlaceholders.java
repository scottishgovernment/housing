package scot.mygov.housing.forms.rentadjudication;

import com.aspose.words.DocumentBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static scot.mygov.documents.PlaceholderUtils.*;

public class RentAdjudicationPlaceholders {

    private RentAdjudicationPlaceholders() {
        // do not instantiate
    }

    public static Map<String, Consumer<DocumentBuilder>> placeholders() {
        Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();

        put(placeholders, lines(5),
                "tenants", "tenantsAgent",
                "landlords","landlordsAgent",
                "notAvailableForInspection");

        put(placeholders, lines(3),
                "rooms", "sharedAreas", "included",
                "servicesDetails", "servicesCostDetails",
                "tenantImprovements", "landlordImprovements",
                "damages");

        put(placeholders, lines(1),
                "propertyType", "heating", "doubleGlazing");

        placeholders.put("currentRentAmount", inlineMonetaryValue());
        placeholders.put("currentRentFrequency", inline("_________________"));
        placeholders.put("newRentAmount", inlineMonetaryValue());
        placeholders.put("newRentFrequency", inline("_________________"));

        return placeholders;
    }
}
