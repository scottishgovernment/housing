package scot.mygov.housing.postcode;

import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.MapcloudResult;
import scot.mygov.housing.mapcloud.MapcloudResults;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Implement the PostcodeService exeption using a Mapcloud client.
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
                .stream()
                .map(this::toResult)
                .collect(toList());
        PostcodeServiceResults res = new PostcodeServiceResults();
        res.setResults(to);
        return res;
    }

    private PostcodeServiceResult toResult(MapcloudResult from) {
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
        if (!isEmpty(value)) {
            list.add(value);
        }
    }
}
