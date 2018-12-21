package scot.mygov.housing.postcode;

import javafx.geometry.Pos;
import org.junit.Assert;
import org.junit.Test;

import java.nio.channels.Pipe;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PostcodeUtilTest {

    @Test
    public void recognisesKnownScottishPostcodes() {
        List<String> inputs = new ArrayList<>();
        Collections.addAll(inputs,
                "eh104ax",
                " eh104ax",
                "EH10 4AX",
                "HS4 APT");
        Collection<String> scottishPostcodes = inputs.stream().filter(this::scottish).collect(toList());
        assertEquals(inputs, scottishPostcodes);
    }

    @Test
    public void rejectsNonScottishPostcodes() {
        List<String> inputs = new ArrayList<>();
        Collections.addAll(inputs,
                "NE1 1DF",
                "ne11df",
                "NE1 1SA",
                " L4 1SE",
                "CF3 0JN",
                "BT12 7DY");
        Collection<String> scottishPostcodes = inputs.stream().filter(this::scottish).collect(toList());
        assertEquals(emptyList(), scottishPostcodes);
    }

    @Test
    public void rejectsEmptyPostcode() {
        assertFalse(scottish(""));
    }

    @Test
    public void rejectsNullPostcode() {
        assertFalse(scottish(null));
    }

    boolean scottish(String poscode) {
        return PostcodeUtils.country(poscode).equals("Scotland");
    }
}
