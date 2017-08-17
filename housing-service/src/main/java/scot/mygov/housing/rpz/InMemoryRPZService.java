package scot.mygov.housing.rpz;

import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.MapcloudResult;
import scot.mygov.housing.mapcloud.MapcloudResults;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * In memory implementation of RPZ service.
 */
public class InMemoryRPZService implements RPZService {

    private final Map<String, RPZ> rpzByUprn;

    private final Map<String, RPZ> rpzByPostcode;

    private final Mapcloud mapcloud;

    public InMemoryRPZService(Set<RPZ> rentPressureZones, Mapcloud mapcloud) {
        this.mapcloud = mapcloud;

        // map rent ressure zones by uprn and by postcode.
        rpzByUprn = new HashMap<>();
        rpzByPostcode = new HashMap<>();
        rentPressureZones.stream().forEach(rpz -> {
            rpz.getUprns().stream().forEach(uprn -> rpzByUprn.put(uprn, rpz));
            rpz.getPostcodes().stream().forEach(postcode -> rpzByPostcode.put(postcode.toLowerCase(), rpz));
        });
    }

    public RPZResult rpz(String uprn, LocalDate date) throws RPZServiceException {

        // is the uprn in a rent pressure zone?
        RPZ rpz = rpzByUprn.get(uprn);
        if (rpz != null && inDateRange(rpz, date)){
            return new RPZResult.Builder().rpz(rpz).build();
        }

        // fetch the details of the uprn
        String postcode = postcodeForUprn(uprn);
        rpz = rpzByPostcode.get(postcode.toLowerCase());
        if (rpz != null && inDateRange(rpz, date)){
            return new RPZResult.Builder().rpz(rpz).build();
        }

        // this uprn is not in a rpz for this date range
        return new RPZResult.Builder().inRentPressureZone(false).build();
    }

    private boolean inDateRange(RPZ rpz, LocalDate date) {
        return date.isAfter(rpz.getFrom().minusDays(1)) && date.isBefore(rpz.getTo().plusDays(1));
    }

    private String postcodeForUprn(String uprn) throws RPZServiceException {
        try {
            MapcloudResults results = mapcloud.lookupUprn(uprn);
            if (results.getResults().size() != 1) {
                throw new RPZServiceException("Expected one postcode for uprn " + uprn);
            }

            MapcloudResult result = results.getResults().get(0);
            return result.getPostcode();
        } catch (MapcloudException e) {
            throw new RPZServiceException("Failed to fetch postcode for rpz", e);
        }
    }

}
