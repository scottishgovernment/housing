package scot.mygov.housing.postcode;

import java.util.ArrayList;
import java.util.List;

public class PostcodeServiceResults {

    private List<PostcodeServiceResult> results = new ArrayList<>();

    public List<PostcodeServiceResult> getResults() {
        return results;
    }

    public void setResults(List<PostcodeServiceResult> results) {
        this.results = results;
    }
}
