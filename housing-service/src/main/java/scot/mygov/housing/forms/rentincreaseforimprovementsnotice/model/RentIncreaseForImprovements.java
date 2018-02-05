package scot.mygov.housing.forms.rentincreaseforimprovementsnotice.model;

import scot.mygov.housing.forms.AbstractFormModel;
import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.util.ArrayList;
import java.util.List;

public class RentIncreaseForImprovements extends AbstractFormModel {

    private Address address = new Address();

    private List<AgentOrLandLord> landlords = new ArrayList<>();

    private AgentOrLandLord landlordsAgent = new AgentOrLandLord();

    private List<Person> tenants = new ArrayList<>();

    private String improvements;

    private String rentIncreaseAmount;

    private String rentIncreaseFrequency;

    private boolean includesReceipts;

    private boolean includesBeforeAndAfterPictures;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public List<Person> getTenants() {
        return tenants;
    }

    public void setTenants(List<Person> tenants) {
        this.tenants = tenants;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getRentIncreaseAmount() {
        return rentIncreaseAmount;
    }

    public void setRentIncreaseAmount(String rentIncreaseAmount) {
        this.rentIncreaseAmount = rentIncreaseAmount;
    }

    public String getRentIncreaseFrequency() {
        return rentIncreaseFrequency;
    }

    public void setRentIncreaseFrequency(String rentIncreaseFrequency) {
        this.rentIncreaseFrequency = rentIncreaseFrequency;
    }

    public boolean isIncludesReceipts() {
        return includesReceipts;
    }

    public void setIncludesReceipts(boolean includesReceipts) {
        this.includesReceipts = includesReceipts;
    }

    public boolean isIncludesBeforeAndAfterPictures() {
        return includesBeforeAndAfterPictures;
    }

    public void setIncludesBeforeAndAfterPictures(boolean includesBeforeAndAfterPictures) {
        this.includesBeforeAndAfterPictures = includesBeforeAndAfterPictures;
    }
}
