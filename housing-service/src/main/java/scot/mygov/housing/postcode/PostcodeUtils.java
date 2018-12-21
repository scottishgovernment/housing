package scot.mygov.housing.postcode;

import org.apache.commons.lang3.StringUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PostcodeUtils {

    private static final Set<String> SCOTTISH_POSTCODE_PREFIXES;

    static {
        SCOTTISH_POSTCODE_PREFIXES = new HashSet<>();
        Collections.addAll(
                SCOTTISH_POSTCODE_PREFIXES,
                "AB", "DD", "DG", "EH", "FK", "G", "HS", "IV", "KA", "KW", "KY", "ML", "PA", "PH", "TD", "ZE");
    }

    private PostcodeUtils() {
        // hide constructor
    }

    /**
     * Europa does not return a country...temporarily we will just match the prefix against the list of knowsn Scottish
     * postcodes. This is not always going to be right since there are corner cases near the border.
     */
    static String country(String postcode) {
        if (StringUtils.isBlank(postcode)) {
            return "";
        }

        String postcodePrefix = leadingCharacters(postcode.trim());
        boolean isScottishPostcode = SCOTTISH_POSTCODE_PREFIXES.contains(postcodePrefix.toUpperCase());
        return isScottishPostcode ? "Scotland" : "";
    }

    private static String leadingCharacters(String s) {
        StringBuilder leadingCharacters = new StringBuilder();
        final CharacterIterator it = new StringCharacterIterator(s);
        for(char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
            if (!Character.isAlphabetic(c)) {
                break;
            }
            leadingCharacters.append(c);
        }
        return leadingCharacters.toString();
    }
}
