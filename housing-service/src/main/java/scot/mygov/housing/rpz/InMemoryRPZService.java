package scot.mygov.housing.rpz;

import scot.mygov.geosearch.api.models.Postcode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/**
 * In memory implementation of RPZ service.
 */
public class InMemoryRPZService implements RPZService {

    private final PostcodeSource postcodeSource;
    private final Set<RPZ> rentPressureZones;

    public InMemoryRPZService(Set<RPZ> rentPressureZones, PostcodeSource postcodeSource) {
        this.rentPressureZones = rentPressureZones;
        this.postcodeSource = postcodeSource;
    }

    public RPZResult rpz(String postcodeIn, LocalDate date) {
        if (!postcodeSource.validPostcode(postcodeIn)) {
            return new RPZResult.Builder().validPostcode(false).build();
        }

        Postcode postcode = postcodeSource.postcode(postcodeIn);
        if (postcode == null) {
            return new RPZResult.Builder()
                    .validPostcode(true)
                    .scottishPostcode(false)
                    .build();
        }

        // determine if a rent pressure zone exists for this postcode and date
        Optional<RPZ> rpz = rentPressureZones
                .stream()
                .filter(r -> r.getPostcodes().contains(postcode.getPostcode()) &&
                            date.compareTo(r.getFrom()) >= 0 &&
                            date.compareTo(r.getTo()) <= 0)
                .findFirst();

        if (!rpz.isPresent()) {
            return new RPZResult.Builder()
                    .validPostcode(true)
                    .scottishPostcode(true)
                    .inRentPressureZone(false)
                    .build();
        }

        return new RPZResult.Builder()
                .rpz(rpz.get())
                .build();


    }


}
