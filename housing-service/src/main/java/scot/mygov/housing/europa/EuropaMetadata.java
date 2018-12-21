package scot.mygov.housing.europa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropaMetadata {

    private String query;

    private double querytime;

    private int vintage;

    private int count;

    private String addresstype;

    private String status;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public double getQuerytime() {
        return querytime;
    }

    public void setQuerytime(double querytime) {
        this.querytime = querytime;
    }

    public int getVintage() {
        return vintage;
    }

    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String ststus) {
        this.status = ststus;
    }
}
