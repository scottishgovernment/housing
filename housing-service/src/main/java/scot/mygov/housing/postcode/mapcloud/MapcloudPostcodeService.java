package scot.mygov.housing.postcode.mapcloud;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.modeltenancy.validation.ValidationUtil;
import scot.mygov.housing.postcode.PostcodeServiceResult;
import scot.mygov.housing.postcode.PostcodeService;
import scot.mygov.housing.postcode.PostcodeServiceException;
import scot.mygov.housing.postcode.PostcodeServiceResults;
import scot.mygov.housing.rpz.PostcodeSource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MapcloudPostcodeService implements PostcodeService {

    private final PostcodeSource postcodeSource;
    private final WebTarget mapcloudTarget;
    private final String user;
    private final String password;

    public MapcloudPostcodeService(PostcodeSource postcodeSource, WebTarget mapcloudTarget, String user, String password) {
        this.postcodeSource = postcodeSource;
        this.mapcloudTarget = mapcloudTarget;
        this.user = user;
        this.password = password;
    }

    public PostcodeServiceResults lookup(String postcodeIn) throws PostcodeServiceException {

        // make the following code case insensitive
        String postcode = postcodeIn.toUpperCase();

        // is this a valid postcode?
        if (!ValidationUtil.validPostcode(postcode)) {
            return invalidPostcodeResults();
        }

        // is this a Scottish postcode?
        Postcode postcodeObj = postcodeSource.postcode(postcode);
        if (postcodeObj == null) {
            return nonScottishPostcodeResult();
        }

        // call the mapcloud service
        return resultFromLookup(postcodeObj);
    }

    private PostcodeServiceResults resultFromLookup(Postcode postcode) throws PostcodeServiceException {
        MapcloudPostcodeResults mapcloudResults = performLookup(postcode.getNormalisedPostcode());
        List<PostcodeServiceResult> results = mapcloudResults.getResults().stream().map(this::toResult).collect(toList());
        PostcodeServiceResults res = new PostcodeServiceResults();
        res.setValidPostcode(true);
        res.setScottishPostcode(true);
        res.setResults(results);
        return res;
    }

    private PostcodeServiceResults invalidPostcodeResults() {
        PostcodeServiceResults res = new PostcodeServiceResults();
        res.setValidPostcode(false);
        res.setScottishPostcode(false);
        return res;
    }

    private PostcodeServiceResults nonScottishPostcodeResult() {
        PostcodeServiceResults res = new PostcodeServiceResults();
        res.setValidPostcode(true);
        res.setScottishPostcode(false);
        return res;
    }

    private PostcodeServiceResult toResult(MapcloudPostcodeResult from) {
        PostcodeServiceResult to = new PostcodeServiceResult();
        to.setUprn(from.getUprn());
        List<String> addessLines = new ArrayList<>();
        addIfNotEmpty(from.getAddressLine1(), addessLines);
        addIfNotEmpty(from.getAddressLine2(), addessLines);
        addIfNotEmpty(from.getAddressLine3(), addessLines);
        to.setAddressLines(addessLines);
        to.setTown(from.getTown());
        to.setPostcode(from.getPostcode());
        return to;
    }

    private void addIfNotEmpty(String value, List<String> list) {
        if (!StringUtils.isEmpty(value)) {
            list.add(value);
        }
    }

    private MapcloudPostcodeResults performLookup(String postcode) throws PostcodeServiceException {

        Response response = mapcloudTarget
                .path("address/addressbase/postcode")
                .queryParam("pc", postcode)
                .queryParam("addrformat", 2)
                .request()
                .header("Authorization", authHeader())
                .get();

        if (response.getStatus() == 200) {
            return response.readEntity(MapcloudPostcodeResults.class);
        }

        // we got an error from mapcloud, throw as an exception
        String message = "Mapcloud returned error"+response.getStatus() + response.getEntity().toString();
        throw new PostcodeServiceException(message);
    }

    private String authHeader() {
        String userAndPassword = user + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(userAndPassword.getBytes());
        return "Basic " + encoded;
    }
}
