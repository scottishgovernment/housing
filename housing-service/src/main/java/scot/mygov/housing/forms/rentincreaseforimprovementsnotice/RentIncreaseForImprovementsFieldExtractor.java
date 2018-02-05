package scot.mygov.housing.forms.rentincreaseforimprovementsnotice;

import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.FieldExtractorUtils;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.housing.forms.rentincreaseforimprovementsnotice.model.RentIncreaseForImprovements;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singleton;
import static scot.mygov.housing.forms.FieldExtractorUtils.extractPeople;

public class RentIncreaseForImprovementsFieldExtractor implements FieldExtractor<RentIncreaseForImprovements> {

    public Map<String, Object> extractFields(RentIncreaseForImprovements model) {

        Map<String, Object> fields = new HashMap<>();
        FieldExtractorUtils.extractLandlords(fields, model.getLandlords());
        extractPeople("landlordsAgent", fields, singleton(model.getLandlordsAgent()));
        extractPeople("tenantDetails", fields, model.getTenants());
        fields.put("address", FieldExtractorUtils.addressFieldsMultipleLines(model.getAddress()));
        fields.put("improvements", model.getImprovements());
        fields.put("rentIncreaseAmount", model.getRentIncreaseAmount());
        fields.put("rentIncreaseFrequency", RentPaymentFrequency.description(model.getRentIncreaseFrequency()));
        if (model.isIncludesReceipts()) {
            fields.put("includesReceipts", "X");
        } else {
            fields.put("includesReceipts", "_");
        }

        if (model.isIncludesBeforeAndAfterPictures()) {
            fields.put("includesBeforeAndAfterPictures", "X");
        } else {
            fields.put("includesBeforeAndAfterPictures", "_");
        }
        return fields;

    }


}
