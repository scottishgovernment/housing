package scot.mygov.housing.modeltenancy.model;

public class Service {
    private String name;
    private String value;
    private ResponsiblePersonType responsiblePersonType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ResponsiblePersonType getResponsiblePersonType() {
        return responsiblePersonType;
    }

    public void setResponsiblePersonType(ResponsiblePersonType responsiblePersonType) {
        this.responsiblePersonType = responsiblePersonType;
    }
}