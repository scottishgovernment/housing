package scot.mygov.housing.forms.nonprovisionofdocumentation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import scot.mygov.housing.forms.AbstractFormModel;
import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NonProvisionOfDocumentation extends AbstractFormModel {

    private AgentOrLandLord landLordOrAgent = new AgentOrLandLord();
    private List<String> tenantNames = new ArrayList<>();
    private Person tenantsAgent = new Person();
    private Address address = new Address();
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate intendedReferalDate = null;

    private boolean section10Failure = false;
    private String section10Details = "";
    private boolean section11Failure = false;
    private String section11Details = "";
    private boolean section16Failure = false;

    public AgentOrLandLord getLandLordOrAgent() {
        return landLordOrAgent;
    }

    public void setLandLordOrAgent(AgentOrLandLord landLordOrAgent) {
        this.landLordOrAgent = landLordOrAgent;
    }

    public List<String> getTenantNames() {
        return tenantNames;
    }

    public void setTenantNames(List<String> tenantNames) {
        this.tenantNames = tenantNames;
    }

    public Person getTenantsAgent() {
        return tenantsAgent;
    }

    public void setTenantsAgent(Person tenantsAgent) {
        this.tenantsAgent = tenantsAgent;
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

    public boolean isSection16Failure() {
        return section16Failure;
    }

    public void setSection16Failure(boolean section16Failure) {
        this.section16Failure = section16Failure;
    }
}
