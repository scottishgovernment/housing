package scot.mygov.housing.postcode;

import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.europa.EuropaAddress;
import scot.mygov.housing.europa.EuropaException;
import scot.mygov.housing.europa.EuropaResults;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class EuropaPostcodeService implements PostcodeService {

    private final Europa europa;


    private static final Map<String, String> COUNTRY_MAP;

    static {
        COUNTRY_MAP = new HashMap<>();
        COUNTRY_MAP.put("E", "England");
        COUNTRY_MAP.put("S", "Scotland");
        COUNTRY_MAP.put("W", "Wales");
    }

    @Inject
    public EuropaPostcodeService(Europa europa) {
        this.europa = europa;
    }

    public PostcodeServiceResults lookup(String postcode) throws PostcodeServiceException {
        try {
            return toResults(europa.lookupPostcode(postcode));
        } catch (EuropaException e) {
            throw new PostcodeServiceException("Postcode lookup failed.", e);
        }
    }

    private PostcodeServiceResults toResults(EuropaResults from) {
        List<PostcodeServiceResult> to = from.getMetadata().getCount() == 0
                ? Collections.emptyList()
                : from.getResults().get(0).getAddress().stream().map(this::toResult).sorted(PostcodeServiceResult.comparator()).collect(toList());
        PostcodeServiceResults res = new PostcodeServiceResults();
        res.setResults(to);
        return res;
    }

    private PostcodeServiceResult toResult(EuropaAddress from) {
        PostcodeServiceResult to = new PostcodeServiceResult();
        to.setUprn(from.getUprn());
        to.setBuilding(building(from));
        to.setOrg(organisation(from));
        to.setStreet(street(from));
        to.setLocality(locality(from));
        to.setTown(from.getTown());
        to.setPostcode(from.getPostcode());
        to.setCountry(COUNTRY_MAP.getOrDefault(from.getCountry(), from.getCountry()));
        return to;
    }

    String organisation(EuropaAddress from) {
        return combineFields(from.getDepartmentName(), from.getOrganisationName());
    }

    String building(EuropaAddress from) {
        return combineFields(
                poBox(from),
                from.getBuildingNumber(),
                from.getSubBuildingName(),
                from.getBuildingName());
    }

    String poBox(EuropaAddress from) {
        return StringUtils.isNotBlank(from.getPobox())
                ? String.format("PO Box %s", from.getPobox())
                : null;
    }

    String street(EuropaAddress from) {
        return combineFields(
                from.getThoroughfare(),
                from.getDependentThoroughfare());
    }

    String locality(EuropaAddress from) {
        return combineFields(
                from.getDoubleDependentLocality(),
                from.getDependentLocality());
    }

    String combineFields(String ...fields) {
        return Arrays.stream(fields)
                .filter(StringUtils::isNotBlank)
                .collect(joining(" "));
    }
}
