package scot.mygov.housing.europa;

import org.junit.Test;

import static org.junit.Assert.*;

public class EuropaResultsTest {

    @Test
    public void defaultHasNoResults() {
        assertFalse(new EuropaResults().hasResults());
    }

    @Test
    public void recognisesResults() {
        EuropaResults results = new EuropaResults();
        results.getResults().add(new AddressResultWrapper());
        results.getResults().get(0).getAddress().add(new EuropaAddress());
        assertTrue(results.hasResults());


    }
}
