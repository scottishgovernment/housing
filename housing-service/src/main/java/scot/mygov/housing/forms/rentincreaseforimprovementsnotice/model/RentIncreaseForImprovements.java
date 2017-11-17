package scot.mygov.housing.forms.rentincreaseforimprovementsnotice.model;

import scot.mygov.housing.forms.AbstractFormModel;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.util.ArrayList;
import java.util.List;

public class RentIncreaseForImprovements extends AbstractFormModel {

    private List<Person> tenants = new ArrayList<>();
    private String inRentPressureZone;
    private List<AgentOrLandLord> landlords = new ArrayList<>();
    private AgentOrLandLord landlordsAgent = new AgentOrLandLord();

    public List<Person> getTenants() {
        return tenants;
    }

    public void setTenants(List<Person> tenants) {
        this.tenants = tenants;
    }

    public String getInRentPressureZone() {
        return inRentPressureZone;
    }

    public void setInRentPressureZone(String inRentPressureZone) {
        this.inRentPressureZone = inRentPressureZone;
    }

    public List<AgentOrLandLord> getLandlords() {
        return landlords;
    }

    public void setLandlords(List<AgentOrLandLord> landlords) {
        this.landlords = landlords;
    }

    public AgentOrLandLord getLandlordsAgent() {
        return landlordsAgent;
    }

    public void setLandlordsAgent(AgentOrLandLord landlordsAgent) {
        this.landlordsAgent = landlordsAgent;
    }
}
