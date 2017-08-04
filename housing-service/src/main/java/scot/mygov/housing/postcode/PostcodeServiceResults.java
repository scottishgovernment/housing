package scot.mygov.housing.postcode;

import java.util.ArrayList;
import java.util.List;

public class PostcodeServiceResults {

    private boolean validPostcode;
    private boolean scottishPostcode;
    private List<PostcodeServiceResult> results = new ArrayList<>();

    public boolean isValidPostcode() {
        return validPostcode;
    }

    public void setValidPostcode(boolean validPostcode) {
        this.validPostcode = validPostcode;
    }

    public boolean isScottishPostcode() {
        return scottishPostcode;
    }

    public void setScottishPostcode(boolean scottishPostcode) {
        this.scottishPostcode = scottishPostcode;
    }

    public List<PostcodeServiceResult> getResults() {
        return results;
    }

    public void setResults(List<PostcodeServiceResult> results) {
        this.results = results;
    }
}
