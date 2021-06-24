package scot.mygov.housing.rpz;

import java.time.LocalDate;

/**
 * Stub implementaiton of RPZService used until we have some rent pressure zones.
 */
public class StubRPZService implements RPZService {

    @Override
    public RPZResult rpz(String uprn, LocalDate date) throws RPZServiceException {
        return new RPZResult.Builder().inRentPressureZone(false).build();
    }
}
