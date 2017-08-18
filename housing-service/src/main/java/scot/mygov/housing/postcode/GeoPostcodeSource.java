package scot.mygov.housing.postcode;

import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.HousingModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A Source of postcodes (get from geo service).
 */
public class GeoPostcodeSource {

    private final WebTarget target;

    @Inject
    public GeoPostcodeSource(
            @Named(HousingModule.GEO_POSTCODES) WebTarget target) {
        this.target = target;
    }

    public Postcode postcode(String postcode) throws PostcodeServiceException {

        try {
            Response response = target.path(postcode).request().get();

            // return null for a 404, this is not a scottish postcode
            if (response.getStatus() == 404) {
                return null;
            }
            if (response.getStatus() == 200) {
                return response.readEntity(Postcode.class);
            }
            throw new PostcodeServiceException("Error from geo:" + response.getEntity().toString());
        } catch (ProcessingException e) {
            throw new PostcodeServiceException("Failed to get postcode from geo", e);
        }
    }
}
