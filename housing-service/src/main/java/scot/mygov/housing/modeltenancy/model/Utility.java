package scot.mygov.housing.modeltenancy.model;

public enum Utility {

    GAS("Gas"),
    ELECTRICITY("Electricity"),
    TELEPHONE("Telephone"),
    INTERNET("Internet / Broadband"),
    TV_LICENCE("TV Licence");

    private final String description;

    private Utility(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
