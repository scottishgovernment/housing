package scot.mygov.housing.europa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropaAddress {

    private String uprn;

    @JsonProperty("dpa_department_name")
    private String departmentName;

    @JsonProperty("dpa_organisation_name")
    private String organisationName;

    @JsonProperty("dpa_sub_building_name")
    private String subBuildingName;

    @JsonProperty("dpa_building_name")
    private String buildingName;

    @JsonProperty("dpa_building_number")
    private String buildingNumber;

    @JsonProperty("dpa_po_box_number")
    private String pobox;

    @JsonProperty("dpa_dependent_thoroughfare")
    private String dependentThoroughfare;

    @JsonProperty("dpa_thoroughfare")
    private String thoroughfare;

    @JsonProperty("dpa_double_dependent_locality")
    private String doubleDependentLocality;

    @JsonProperty("dpa_dependent_locality")
    private String dependentLocality;

    @JsonProperty("dpa_post_town")
    private String town;

    @JsonProperty("dpa_postcode")
    private String postcode;

    private int matchScore;

    public String getUprn() {
        return uprn;
    }

    public void setUprn(String uprn) {
        this.uprn = uprn;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getSubBuildingName() {
        return subBuildingName;
    }

    public void setSubBuildingName(String subBuildingName) {
        this.subBuildingName = subBuildingName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getPobox() {
        return pobox;
    }

    public void setPobox(String pobox) {
        this.pobox = pobox;
    }

    public String getDependentThoroughfare() {
        return dependentThoroughfare;
    }

    public void setDependentThoroughfare(String dependentThoroughfare) {
        this.dependentThoroughfare = dependentThoroughfare;
    }

    public String getThoroughfare() {
        return thoroughfare;
    }

    public void setThoroughfare(String thoroughfare) {
        this.thoroughfare = thoroughfare;
    }

    public String getDoubleDependentLocality() {
        return doubleDependentLocality;
    }

    public void setDoubleDependentLocality(String doubleDependentLocality) {
        this.doubleDependentLocality = doubleDependentLocality;
    }

    public String getDependentLocality() {
        return dependentLocality;
    }

    public void setDependentLocality(String dependentLocality) {
        this.dependentLocality = dependentLocality;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }
}
