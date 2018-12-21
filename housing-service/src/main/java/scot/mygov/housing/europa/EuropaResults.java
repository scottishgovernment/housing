package scot.mygov.housing.europa;

import java.util.ArrayList;
import java.util.List;

public class EuropaResults {

    private EuropaMetadata metadata;

    private List<AddressResultWrapper> results = new ArrayList<>();

    public EuropaMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(EuropaMetadata metadata) {
        this.metadata = metadata;
    }

    public List<AddressResultWrapper> getResults() {
        return results;
    }

    public void setResults(List<AddressResultWrapper> results) {
        this.results = results;
    }

    public boolean hasResults() {
        // the results are wrapped in an array with a single element
        return !results.isEmpty() && !results.get(0).getAddress().isEmpty();

    }
}
