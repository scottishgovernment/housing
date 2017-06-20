package scot.mygov.housing.modeltenancy.model;

/**
 * Created by z418868 on 19/06/2017.
 */
public enum RentPaymentFrequency {

    WEEKLY("week"),
    FORTNIGHTLY("fortnight"),
    EVERY_FOUR_WEEKS("four weeks"),
    CALENDAR_MONTH("calendar month"),
    QUARTERLY("quarter");

    private final String description;

    private RentPaymentFrequency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
