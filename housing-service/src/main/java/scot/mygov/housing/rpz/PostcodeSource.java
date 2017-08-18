package scot.mygov.housing.rpz;

import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.HousingModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A Source of postcodes (get from geo service).
 */
public class PostcodeSource {

    private final WebTarget target;

    @Inject
    public PostcodeSource(
            @Named(HousingModule.GEO_POSTCODES) WebTarget target) {
        this.target = target;
    }

    public Postcode postcode(String postcode) {
        Response response = target.path(postcode).request().get();

        if (response.getStatus() == 404) {
            return null;
        }

        return response.readEntity(Postcode.class);
    }
}
