package scot.mygov.housing.postcode;

import scot.mygov.housing.mapcloud.DPAMapcloudResult;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.MapcloudResults;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Implement the PostcodeService interface using a Mapcloud client.
 */
public class MapcloudPostcodeService implements PostcodeService {

    private final Mapcloud mapcloud;

    @Inject
    public MapcloudPostcodeService(Mapcloud mapcloud) {
        this.mapcloud = mapcloud;
    }

    public PostcodeServiceResults lookup(String postcode) throws PostcodeServiceException {
        try {
            return toResults(mapcloud.lookupPostcode(postcode));
        } catch (MapcloudException e) {
            throw new PostcodeServiceException("Mapcloud lookup failed.", e);
        }
    }

    private PostcodeServiceResults toResults(MapcloudResults from) {
        List<PostcodeServiceResult> to = from.getResults()
                .stream().map(this::toResult).sorted(comparator()).collect(toList());
        PostcodeServiceResults res = new PostcodeServiceResults();
        res.setResults(to);
        return res;
    }

    private Comparator<PostcodeServiceResult> comparator() {
        return Comparator
                .comparing(PostcodeServiceResult::getStreetName)
                .thenComparing(PostcodeServiceResult::getHouseNumber)
                .thenComparing(PostcodeServiceResult::getBuilding);
    }

    private PostcodeServiceResult toResult(DPAMapcloudResult from) {
        PostcodeServiceResult to = new PostcodeServiceResult();
        to.setUprn(from.getUprn());
        to.setBuilding(from.getAddressBuilding());
        to.setOrg(from.getAddressOrg());
        to.setStreet(from.getAddressStreet());
        to.setLocality(from.getAddressLocality());
        to.setTown(from.getPostTown());
        to.setPostcode(from.getPostcode());
        to.setCountry(from.getCountry());
        return to;
    }

}
