package scot.mygov.housing.rpz;

import scot.mygov.geosearch.api.models.Postcode;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A Source of postcodes (get from geo service).
 */
public class PostcodeSource {

    private final WebTarget target;

    public PostcodeSource(WebTarget target) {
        this.target = target;
    }

    public Postcode postcode(String postcode) {
        Response response = target.path(postcode).request().get();
        return response.readEntity(Postcode.class);
    }
}
