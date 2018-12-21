package scot.mygov.housing.postcode;

import java.util.Comparator;

public class PostcodeServiceResult {

    private String uprn;

    private String building;
    private String org;
    private String street;
    private String locality;
    private String town;
    private String postcode;
    private String country;

    public String getUprn() {
        return uprn;
    }

    public void setUprn(String uprn) {
        this.uprn = uprn;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Comparator<PostcodeServiceResult> comparator() {
        return Comparator
                .comparing(PostcodeServiceResult::getStreet)
                .thenComparing(PostcodeServiceResult::getBuilding);
    }
}
