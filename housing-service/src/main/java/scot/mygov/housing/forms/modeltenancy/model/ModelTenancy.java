package scot.mygov.housing.forms.modeltenancy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO for model tenancy data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelTenancy {

    private List<Person> tenants = new ArrayList<>();
    private List<Guarantor> guarantors = new ArrayList<>();
    private AgentOrLandLord lettingAgent;
    private List<AgentOrLandLord> landlords = new ArrayList<>();
    private String communicationsAgreement;
    private Address propertyAddress;
    private String propertyType;
    private List<String> landlordStructureList = new ArrayList<>();
    private String furnishingType;
    private boolean inRentPressureZone;
    private boolean hmoProperty;
    private String hmo24ContactNumber;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate hmoRegistrationExpiryDate;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tenancyStartDate;
    private String rentAmount;
    private String rentPaymentFrequency;
    private boolean rentPayableInAdvance;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate firstPaymentDate;
    private String firstPaymentAmount;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate firstPaymentPeriodStart;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate firstPaymentPeriodEnd;
    private String rentPaymentDayOrDate;
    private String rentPaymentSchedule;
    private String rentPaymentMethod;
    private List<Service> services = new ArrayList<>();
    private List<Facility> facilities = new ArrayList<>();
    private String depositAmount;
    private String tenancyDepositSchemeAdministrator;
    private OptionalTerms optionalTerms = new OptionalTerms();


    public List<Person> getTenants() {
        return tenants;
    }

    public void setTenants(List<Person> tenants) {
        this.tenants = tenants;
    }

    public List<Guarantor> getGuarantors() {
        return guarantors;
    }

    public void setGuarantors(List<Guarantor> guarantors) {
        this.guarantors = guarantors;
    }

    public AgentOrLandLord getLettingAgent() {
        return lettingAgent;
    }

    public void setLettingAgent(AgentOrLandLord lettingAgent) {
        this.lettingAgent = lettingAgent;
    }

    public List<AgentOrLandLord> getLandlords() {
        return landlords;
    }

    public void setLandlords(List<AgentOrLandLord> landlords) {
        this.landlords = landlords;
    }

    public String getCommunicationsAgreement() {
        return communicationsAgreement;
    }

    public void setCommunicationsAgreement(String communicationsAgreement) {
        this.communicationsAgreement = communicationsAgreement;
    }

    public Address getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(Address propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public List<String> getLandlordStructureList() {
        return landlordStructureList;
    }

    public void setLandlordStructureList(List<String> landlordStructureList) {
        this.landlordStructureList = landlordStructureList;
    }

    public String getFurnishingType() {
        return furnishingType;
    }

    public void setFurnishingType(String furnishingType) {
        this.furnishingType = furnishingType;
    }

    public boolean isInRentPressureZone() {
        return inRentPressureZone;
    }

    public void setInRentPressureZone(boolean inRentPressureZone) {
        this.inRentPressureZone = inRentPressureZone;
    }

    public boolean isHmoProperty() {
        return hmoProperty;
    }

    public void setHmoProperty(boolean hmoProperty) {
        this.hmoProperty = hmoProperty;
    }

    public String getHmo24ContactNumber() {
        return hmo24ContactNumber;
    }

    public void setHmo24ContactNumber(String hmo24ContactNumber) {
        this.hmo24ContactNumber = hmo24ContactNumber;
    }

    public LocalDate getHmoRegistrationExpiryDate() {
        return hmoRegistrationExpiryDate;
    }

    public void setHmoRegistrationExpiryDate(LocalDate hmoRegistrationExpiryDate) {
        this.hmoRegistrationExpiryDate = hmoRegistrationExpiryDate;
    }

    public LocalDate getTenancyStartDate() {
        return tenancyStartDate;
    }

    public void setTenancyStartDate(LocalDate tenancyStartDate) {
        this.tenancyStartDate = tenancyStartDate;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(String rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getRentPaymentFrequency() {
        return rentPaymentFrequency;
    }

    public void setRentPaymentFrequency(String rentPaymentFrequency) {
        this.rentPaymentFrequency = rentPaymentFrequency;
    }

    public boolean isRentPayableInAdvance() {
        return rentPayableInAdvance;
    }

    public void setRentPayableInAdvance(boolean rentPayableInAdvance) {
        this.rentPayableInAdvance = rentPayableInAdvance;
    }

    public LocalDate getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(LocalDate firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public String getFirstPaymentAmount() {
        return firstPaymentAmount;
    }

    public void setFirstPaymentAmount(String firstPaymentAmount) {
        this.firstPaymentAmount = firstPaymentAmount;
    }

    public LocalDate getFirstPaymentPeriodStart() {
        return firstPaymentPeriodStart;
    }

    public void setFirstPaymentPeriodStart(LocalDate firstPaymentPeriodStart) {
        this.firstPaymentPeriodStart = firstPaymentPeriodStart;
    }

    public LocalDate getFirstPaymentPeriodEnd() {
        return firstPaymentPeriodEnd;
    }

    public void setFirstPaymentPeriodEnd(LocalDate firstPaymentPeriodEnd) {
        this.firstPaymentPeriodEnd = firstPaymentPeriodEnd;
    }

    public String getRentPaymentDayOrDate() {
        return rentPaymentDayOrDate;
    }

    public void setRentPaymentDayOrDate(String rentPaymentDayOrDate) {
        this.rentPaymentDayOrDate = rentPaymentDayOrDate;
    }

    public String getRentPaymentSchedule() {
        return rentPaymentSchedule;
    }

    public void setRentPaymentSchedule(String rentPaymentSchedule) {
        this.rentPaymentSchedule = rentPaymentSchedule;
    }

    public String getRentPaymentMethod() {
        return rentPaymentMethod;
    }

    public void setRentPaymentMethod(String rentPaymentMethod) {
        this.rentPaymentMethod = rentPaymentMethod;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getTenancyDepositSchemeAdministrator() {
        return tenancyDepositSchemeAdministrator;
    }

    public void setTenancyDepositSchemeAdministrator(String tenancyDepositSchemeAdministrator) {
        this.tenancyDepositSchemeAdministrator = tenancyDepositSchemeAdministrator;
    }

    public OptionalTerms getOptionalTerms() {
        return optionalTerms;
    }

    public void setOptionalTerms(OptionalTerms optionalTerms) {
        this.optionalTerms = optionalTerms;
    }
}
