package scot.mygov.housing.postcode;

import org.apache.commons.lang3.StringUtils;

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

    // return the name of the street
    public String getStreetName() {
        if (StringUtils.isEmpty(street)) {
            return "";
        }

        // if there is no house number in the street then just return street
        if (getHouseNumber() == 0) {
            return street;
        }

        // return the part after the first space if there is one
        int firstSpaceIndex = street.indexOf(" ");
        if (firstSpaceIndex == -1) {
            return street;
        }

        return StringUtils.substringAfter(street, " ");
    }

    // determine the house number
    public int getHouseNumber() {

        // return 0 for empty or null street
        if (StringUtils.isEmpty(street)) {
            return 0;
        }

        // move past any numbers
        int i = 0;
        while (Character.isDigit(street.charAt(i))) {
            i++;
        }
        String numberString = street.substring(0, i);
        if (StringUtils.isEmpty(numberString)) {
            return 0;
        }
        return Integer.parseInt(numberString);
    }
}
