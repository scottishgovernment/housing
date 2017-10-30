package scot.mygov.housing.forms.nonprovisionofdocumentation.model;

import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.time.LocalDate;
import java.util.List;

public class NonProvisionOfDocumentation {

    private AgentOrLandLord landLord;
    private List<Person> tenants;
    private Person tenentsAgent;
    private Address address;
    private LocalDate intendedReferalDate;

    private boolean section10Failure;
    private String section10Details;
    private boolean section11Failure;
    private String section11Details;

    private boolean booleanSection16Failure;

    public AgentOrLandLord getLandLord() {
        return landLord;
    }

    public void setLandLord(AgentOrLandLord landLord) {
        this.landLord = landLord;
    }

    public List<Person> getTenants() {
        return tenants;
    }

    public void setTenants(List<Person> tenants) {
        this.tenants = tenants;
    }

    public Person getTenentsAgent() {
        return tenentsAgent;
    }

    public void setTenentsAgent(Person tenentsAgent) {
        this.tenentsAgent = tenentsAgent;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getIntendedReferalDate() {
        return intendedReferalDate;
    }

    public void setIntendedReferalDate(LocalDate intendedReferalDate) {
        this.intendedReferalDate = intendedReferalDate;
    }

    public boolean isSection10Failure() {
        return section10Failure;
    }

    public void setSection10Failure(boolean section10Failure) {
        this.section10Failure = section10Failure;
    }

    public String getSection10Details() {
        return section10Details;
    }

    public void setSection10Details(String section10Details) {
        this.section10Details = section10Details;
    }

    public boolean isSection11Failure() {
        return section11Failure;
    }

    public void setSection11Failure(boolean section11Failure) {
        this.section11Failure = section11Failure;
    }

    public String getSection11Details() {
        return section11Details;
    }

    public void setSection11Details(String section11Details) {
        this.section11Details = section11Details;
    }

    public boolean isBooleanSection16Failure() {
        return booleanSection16Failure;
    }

    public void setBooleanSection16Failure(boolean booleanSection16Failure) {
        this.booleanSection16Failure = booleanSection16Failure;
    }
}
