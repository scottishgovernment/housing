package scot.mygov.housing.forms.nonprovisionofdocumentation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import scot.mygov.housing.forms.AbstractFormModel;
import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NonProvisionOfDocumentation extends AbstractFormModel {

    private List<AgentOrLandLord> landlords = new ArrayList<>();
    private AgentOrLandLord landlordsAgent = new AgentOrLandLord();
    private List<String> tenantNames = new ArrayList<>();
    private Person tenantsAgent = new Person();
    private Address address = new Address();
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate intendedReferralDate = null;

    private boolean section10Failure = false;
    private String section10Details = "";
    private boolean section11Failure = false;
    private String section11Details = "";
    private boolean section16Failure = false;

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

    public LocalDate getIntendedReferralDate() {
        return intendedReferralDate;
    }

    public void setIntendedReferralDate(LocalDate intendedReferralDate) {
        this.intendedReferralDate = intendedReferralDate;
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
