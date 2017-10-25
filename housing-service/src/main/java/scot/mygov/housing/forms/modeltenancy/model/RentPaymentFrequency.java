package scot.mygov.housing.forms.modeltenancy.model;

import org.apache.commons.lang3.StringUtils;

public enum RentPaymentFrequency {

    WEEKLY("week"),
    FORTNIGHTLY("fortnight"),
    EVERY_FOUR_WEEKS("four weeks"),
    CALENDAR_MONTH("calendar month"),
    QUARTERLY("quarter"),
    YEARLY("yearly");

    private final String description;

    private RentPaymentFrequency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String description(String rpf) {
        if (StringUtils.isEmpty(rpf)) {
            return "";
        }
        return RentPaymentFrequency.valueOf(rpf).getDescription();
    }
}
