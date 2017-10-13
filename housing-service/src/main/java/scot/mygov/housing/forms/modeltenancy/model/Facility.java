package scot.mygov.housing.forms.modeltenancy.model;

public class Facility {

    private String name;
    private FacilityType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }
}
