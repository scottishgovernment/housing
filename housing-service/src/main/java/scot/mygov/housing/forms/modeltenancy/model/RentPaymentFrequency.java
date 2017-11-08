package scot.mygov.housing.forms.modeltenancy.model;

import org.apache.commons.lang3.StringUtils;

public enum RentPaymentFrequency {

    WEEKLY("week", "day of each week"),
    FORTNIGHTLY("fortnight", "day of each fortnight"),
    EVERY_FOUR_WEEKS("four weeks", "day of each four weekly period"),
    CALENDAR_MONTH("calendar month", "date each calendar month"),
    QUARTERLY("quarter", "date each quarter"),
    EVERY_SIX_MONTHS("six months", "date each 6-month period"),
    YEARLY("yearly", "date each year");

    private final String description;
    private final String dayOrDate;

    private RentPaymentFrequency(String description, String dayOrDate) {
        this.description = description;
        this.dayOrDate = dayOrDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOrDate() {
        return dayOrDate;
    }

    public static String description(String rpf) {
        if (StringUtils.isEmpty(rpf)) {
            return "";
        }
        return RentPaymentFrequency.valueOf(rpf).getDescription();
    }

    public static String dayOrDate(String rpf) {
        if (StringUtils.isEmpty(rpf)) {
            return "";
        }
        return RentPaymentFrequency.valueOf(rpf).getDayOrDate();
    }
}
