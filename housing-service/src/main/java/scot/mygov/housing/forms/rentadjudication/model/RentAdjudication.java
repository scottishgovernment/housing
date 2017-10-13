package scot.mygov.housing.forms.rentadjudication.model;

import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;

import java.util.List;

public class RentAdjudication {

    private List<Person> tenants;
    private Person tenantAgent;
    private List<Person> landlords;
    private Person landlordAgent;
    private String propertyType;
    private List<Room> rooms;
    private String sharedAreas;
    private String included;
    private String heating;
    private String doubleGlazing;
    private String servicesDetails;
    private String servicesCostDetails;
    private String furnished;
    private String improvementsTenant;
    private String improvementsLandlord;
    private String damage;
    private String currentRentAmount;
    private RentPaymentFrequency currentRentFrequency;
    private String newRentAmount;
    private RentPaymentFrequency newRentFrequency;

    public List<Person> getTenants() {
        return tenants;
    }

    public void setTenants(List<Person> tenants) {
        this.tenants = tenants;
    }

    public Person getTenantAgent() {
        return tenantAgent;
    }

    public void setTenantAgent(Person tenantAgent) {
        this.tenantAgent = tenantAgent;
    }

    public List<Person> getLandlords() {
        return landlords;
    }

    public void setLandlords(List<Person> landlords) {
        this.landlords = landlords;
    }

    public Person getLandlordAgent() {
        return landlordAgent;
    }

    public void setLandlordAgent(Person landlordAgent) {
        this.landlordAgent = landlordAgent;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getSharedAreas() {
        return sharedAreas;
    }

    public void setSharedAreas(String sharedAreas) {
        this.sharedAreas = sharedAreas;
    }

    public String getIncluded() {
        return included;
    }

    public void setIncluded(String included) {
        this.included = included;
    }

    public String getHeating() {
        return heating;
    }

    public void setHeating(String heating) {
        this.heating = heating;
    }

    public String getDoubleGlazing() {
        return doubleGlazing;
    }

    public void setDoubleGlazing(String doubleGlazing) {
        this.doubleGlazing = doubleGlazing;
    }

    public String getServicesDetails() {
        return servicesDetails;
    }

    public void setServicesDetails(String servicesDetails) {
        this.servicesDetails = servicesDetails;
    }

    public String getServicesCostDetails() {
        return servicesCostDetails;
    }

    public void setServicesCostDetails(String servicesCostDetails) {
        this.servicesCostDetails = servicesCostDetails;
    }

    public String getFurnished() {
        return furnished;
    }

    public void setFurnished(String furnished) {
        this.furnished = furnished;
    }

    public String getImprovementsTenant() {
        return improvementsTenant;
    }

    public void setImprovementsTenant(String improvementsTenant) {
        this.improvementsTenant = improvementsTenant;
    }

    public String getImprovementsLandlord() {
        return improvementsLandlord;
    }

    public void setImprovementsLandlord(String improvementsLandlord) {
        this.improvementsLandlord = improvementsLandlord;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getCurrentRentAmount() {
        return currentRentAmount;
    }

    public void setCurrentRentAmount(String currentRentAmount) {
        this.currentRentAmount = currentRentAmount;
    }

    public RentPaymentFrequency getCurrentRentFrequency() {
        return currentRentFrequency;
    }

    public void setCurrentRentFrequency(RentPaymentFrequency currentRentFrequency) {
        this.currentRentFrequency = currentRentFrequency;
    }

    public String getNewRentAmount() {
        return newRentAmount;
    }

    public void setNewRentAmount(String newRentAmount) {
        this.newRentAmount = newRentAmount;
    }

    public RentPaymentFrequency getNewRentFrequency() {
        return newRentFrequency;
    }

    public void setNewRentFrequency(RentPaymentFrequency newRentFrequency) {
        this.newRentFrequency = newRentFrequency;
    }
}
