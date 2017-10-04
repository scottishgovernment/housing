package scot.mygov.housing.mapcloud;

import java.util.List;

public class MapcloudResults {

    private String query;
    private int totalResults;
    private int maxResults;
    private String format;
    private String dataType;
    private String addressType;
    private String errorCode;
    private List<DPAMapcloudResult> results;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<DPAMapcloudResult> getResults() {
        return results;
    }

    public void setResults(List<DPAMapcloudResult> results) {
        this.results = results;
    }
}
