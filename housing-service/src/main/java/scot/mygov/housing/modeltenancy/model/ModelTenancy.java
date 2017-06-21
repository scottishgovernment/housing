package scot.mygov.housing.modeltenancy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO for model tenancy data
 */
public class ModelTenancy {
    private List<Person> tenants = new ArrayList<>();
    private List<Guarantor> guarantors = new ArrayList<>();
    private AgentOrLandLord lettingAgent;
    private List<AgentOrLandLord> landlords = new ArrayList<>();
    private String communicationsAgreement;
    private Address propertyAddress;
    private String propertyType;
    private String includedAreasOrFacilities;
    private String sharedFacilities;
    private String excludedAreasFacilities;
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
    private String rentPaymentDay;
    private String rentPaymentMethod;
    private String servicesIncludedInRent;
    private String depositAmount;
    private String tenancyDepositSchemeAdministrator;
    private String tenancyDepositSchemeContactDetails;
    private List<String> tenantUtilitiesResponsibilities = new ArrayList<>();

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

    public String getIncludedAreasOrFacilities() {
        return includedAreasOrFacilities;
    }

    public void setIncludedAreasOrFacilities(String includedAreasOrFacilities) {
        this.includedAreasOrFacilities = includedAreasOrFacilities;
    }

    public String getSharedFacilities() {
        return sharedFacilities;
    }

    public void setSharedFacilities(String sharedFacilities) {
        this.sharedFacilities = sharedFacilities;
    }

    public String getExcludedAreasFacilities() {
        return excludedAreasFacilities;
    }

    public void setExcludedAreasFacilities(String excludedAreasFacilities) {
        this.excludedAreasFacilities = excludedAreasFacilities;
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

    public String getRentPaymentDay() {
        return rentPaymentDay;
    }

    public void setRentPaymentDay(String rentPaymentDay) {
        this.rentPaymentDay = rentPaymentDay;
    }

    public String getRentPaymentMethod() {
        return rentPaymentMethod;
    }

    public void setRentPaymentMethod(String rentPaymentMethod) {
        this.rentPaymentMethod = rentPaymentMethod;
    }

    public String getServicesIncludedInRent() {
        return servicesIncludedInRent;
    }

    public void setServicesIncludedInRent(String servicesIncludedInRent) {
        this.servicesIncludedInRent = servicesIncludedInRent;
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

    public String getTenancyDepositSchemeContactDetails() {
        return tenancyDepositSchemeContactDetails;
    }

    public void setTenancyDepositSchemeContactDetails(String tenancyDepositSchemeContactDetails) {
        this.tenancyDepositSchemeContactDetails = tenancyDepositSchemeContactDetails;
    }

    public List<String> getTenantUtilitiesResponsibilities() {
        return tenantUtilitiesResponsibilities;
    }

    public void setTenantUtilitiesResponsibilities(List<String> tenantUtilitiesResponsibilities) {
        this.tenantUtilitiesResponsibilities = tenantUtilitiesResponsibilities;
    }
}
