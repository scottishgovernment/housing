package scot.mygov.housing.rpz;

import scot.mygov.geosearch.api.models.Postcode;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Source of postcodes (get from geo service).
 */
public class PostcodeSource {

    private static final String POSCODE_REGEX = "^[A-Z]{1,2}[0-9R][0-9A-Z]?[0-9][ABD-HJLNP-UW-Z]{2}$";
    private static final Pattern POSTCODE_PATTERN = Pattern.compile(POSCODE_REGEX);
    private final WebTarget target;

    public PostcodeSource(WebTarget target) {
        this.target = target;
    }

    public Postcode postcode(String postcode) {
        Response response = target.path(postcode).request().get();
        return response.readEntity(Postcode.class);
    }

    public boolean validPostcode(String postcode) {
        String noSpaces = postcode.replaceAll(" ", "").toUpperCase();
        Matcher matcher = POSTCODE_PATTERN.matcher(noSpaces);
        return matcher.matches();
    }
}
