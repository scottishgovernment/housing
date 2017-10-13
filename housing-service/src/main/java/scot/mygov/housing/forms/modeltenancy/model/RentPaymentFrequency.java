package scot.mygov.housing.forms.modeltenancy.model;

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
}
