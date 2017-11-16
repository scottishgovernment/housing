package scot.mygov.housing.forms.rentincreasenotice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import scot.mygov.housing.forms.AbstractFormModel;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentIncrease extends AbstractFormModel {

    private List<Person> tenants = new ArrayList<>();
    private String inRentPressureZone;
    private List<AgentOrLandLord> landlords = new ArrayList<>();
    private AgentOrLandLord landlordsAgent = new AgentOrLandLord();
    private String oldRentAmount = "";
    private String oldRentPeriod = "";
    private String newRentAmount = "";
    private String newRentPeriod = "";
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate rentIncreaseDate;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastRentIncreaseDate;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate capFromDate;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate capToDate;

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

    public String getOldRentAmount() {
        return oldRentAmount;
    }

    public void setOldRentAmount(String oldRentAmount) {
        this.oldRentAmount = oldRentAmount;
    }

    public String getOldRentPeriod() {
        return oldRentPeriod;
    }

    public void setOldRentPeriod(String oldRentPeriod) {
        this.oldRentPeriod = oldRentPeriod;
    }

    public String getNewRentAmount() {
        return newRentAmount;
    }

    public void setNewRentAmount(String newRentAmount) {
        this.newRentAmount = newRentAmount;
    }

    public String getNewRentPeriod() {
        return newRentPeriod;
    }

    public void setNewRentPeriod(String newRentPeriod) {
        this.newRentPeriod = newRentPeriod;
    }

    public LocalDate getRentIncreaseDate() {
        return rentIncreaseDate;
    }

    public void setRentIncreaseDate(LocalDate rentIncreaseDate) {
        this.rentIncreaseDate = rentIncreaseDate;
    }

    public LocalDate getLastRentIncreaseDate() {
        return lastRentIncreaseDate;
    }

    public void setLastRentIncreaseDate(LocalDate lastRentIncreaseDate) {
        this.lastRentIncreaseDate = lastRentIncreaseDate;
    }

    public LocalDate getCapFromDate() {
        return capFromDate;
    }

    public void setCapFromDate(LocalDate capFromDate) {
        this.capFromDate = capFromDate;
    }

    public LocalDate getCapToDate() {
        return capToDate;
    }

    public void setCapToDate(LocalDate capToDate) {
        this.capToDate = capToDate;
    }
}
