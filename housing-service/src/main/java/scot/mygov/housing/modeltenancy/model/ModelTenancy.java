package scot.mygov.housing.modeltenancy.model;

import scot.mygov.housing.modeltenancy.Tenant;

import java.time.LocalDate;
import java.util.List;

/**
 * POJO for model tenancy data
 */
public class ModelTenancy {

    private List<Tenant> tenants;
    private AgentOrLandLord lettingAgent;
    private List<AgentOrLandLord> landlords;
    private String communicationsAgreement;
    private String propertyAddress;
    private String typeOfProperty;
    private List<String> includedAreasOrFacilities;
    private List<String> sharedFacilities;
    private List<String> excludedAreasFacilities;
    private List<String> landlordStructureList;

    private String furnishing;
    private boolean inRentPressureZone;
    private boolean hmoProperty;
    private String hmo24ContactNumber;
    private LocalDate hmoRegistrationExiryDate;
    private LocalDate tenancyStartDate;
    private String rentAmount;
    private String rentPaymentFrequency;
    private String rentPayableInAdvance;
    private LocalDate firstPaymentDate;
    private String firstPaymentAmount;
    private LocalDate firstPaymentPerionStart;
    private LocalDate firstPaymentPerionEnd;
    private String rentPaymentDay;
    private String rentPaymentMethod;

    private String servicesIncludedInRent;
    private String depositAmount;
    private String tenancyDepositSchemeAdministrator;
    private String tenancyDepositSchemeContactDetails;
    private String tenantUtilitiesResponsibility;
    private String guarantorName;
    private String guarantorAddress;

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
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

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getTypeOfProperty() {
        return typeOfProperty;
    }

    public void setTypeOfProperty(String typeOfProperty) {
        this.typeOfProperty = typeOfProperty;
    }

    public List<String> getIncludedAreasOrFacilities() {
        return includedAreasOrFacilities;
    }

    public void setIncludedAreasOrFacilities(List<String> includedAreasOrFacilities) {
        this.includedAreasOrFacilities = includedAreasOrFacilities;
    }

    public List<String> getSharedFacilities() {
        return sharedFacilities;
    }

    public void setSharedFacilities(List<String> sharedFacilities) {
        this.sharedFacilities = sharedFacilities;
    }

    public List<String> getExcludedAreasFacilities() {
        return excludedAreasFacilities;
    }

    public void setExcludedAreasFacilities(List<String> excludedAreasFacilities) {
        this.excludedAreasFacilities = excludedAreasFacilities;
    }

    public List<String> getLandlordStructureList() {
        return landlordStructureList;
    }

    public void setLandlordStructureList(List<String> landlordStructureList) {
        this.landlordStructureList = landlordStructureList;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
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

    public LocalDate getHmoRegistrationExiryDate() {
        return hmoRegistrationExiryDate;
    }

    public void setHmoRegistrationExiryDate(LocalDate hmoRegistrationExiryDate) {
        this.hmoRegistrationExiryDate = hmoRegistrationExiryDate;
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

    public String getRentPayableInAdvance() {
        return rentPayableInAdvance;
    }

    public void setRentPayableInAdvance(String rentPayableInAdvance) {
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

    public LocalDate getFirstPaymentPerionStart() {
        return firstPaymentPerionStart;
    }

    public void setFirstPaymentPerionStart(LocalDate firstPaymentPerionStart) {
        this.firstPaymentPerionStart = firstPaymentPerionStart;
    }

    public LocalDate getFirstPaymentPerionEnd() {
        return firstPaymentPerionEnd;
    }

    public void setFirstPaymentPerionEnd(LocalDate firstPaymentPerionEnd) {
        this.firstPaymentPerionEnd = firstPaymentPerionEnd;
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

    public String getTenantUtilitiesResponsibility() {
        return tenantUtilitiesResponsibility;
    }

    public void setTenantUtilitiesResponsibility(String tenantUtilitiesResponsibility) {
        this.tenantUtilitiesResponsibility = tenantUtilitiesResponsibility;
    }

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getGuarantorAddress() {
        return guarantorAddress;
    }

    public void setGuarantorAddress(String guarantorAddress) {
        this.guarantorAddress = guarantorAddress;
    }
}
