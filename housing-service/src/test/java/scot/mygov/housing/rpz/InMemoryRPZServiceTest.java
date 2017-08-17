package scot.mygov.housing.rpz;

import org.junit.Test;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.MapcloudResult;
import scot.mygov.housing.mapcloud.MapcloudResults;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class InMemoryRPZServiceTest {

    @Test
    public void uprnInRpzAndWithinDateRange() throws RPZServiceException, MapcloudException{
        // ARRANGE
        RPZ rpz = rpZForUprn("uprInRPZ");
        RPZService sut = new InMemoryRPZService(singleton(rpz), anyMapcloud());

        // ACT
        RPZResult actual = sut.rpz("uprInRPZ", now());

        // ASSERT
        assertTrue(actual.isInRentPressureZone());
        assertEquals(rpz.getMaxRentIncrease(), actual.getMaxIncrease(), 0);
    }

    @Test
    public void uprnInRpzAndOutwithDateRange() throws RPZServiceException, MapcloudException {
        // ARRANGE
        RPZ rpz = rpZForUprn("uprInRPZ");
        RPZService sut = new InMemoryRPZService(singleton(rpz), anyMapcloud());

        // ACT
        RPZResult actual = sut.rpz("uprInRPZ", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test
    public void postcodeInRpzAndWithinDateRange() throws RPZServiceException, MapcloudException {
        // ARRANGE
        RPZ rpz = rpZForPostcode("EH104AX");
        Mapcloud mapcloud = mapcloudWithUprnResults("uprn", mapcloudResults("uprn", "EH104AX"));
        RPZService sut = new InMemoryRPZService(singleton(rpz), mapcloud);

        // ACT
        RPZResult actual = sut.rpz("uprn", now());

        // ASSERT
        assertTrue(actual.isInRentPressureZone());
    }

    @Test
    public void postcodeInRpzAndOutwithDateRange() throws RPZServiceException, MapcloudException {
        // ARRANGE
        RPZ rpz = rpZForPostcode("EH104AX");
        Mapcloud mapcloud = mapcloudWithUprnResults("uprn", mapcloudResults("uprn", "EH104AX"));
        RPZService sut = new InMemoryRPZService(singleton(rpz), mapcloud);

        // ACT
        RPZResult actual = sut.rpz("uprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }

    @Test(expected = RPZServiceException.class)
    public void mapCloudExceptionWrappedAsExpected() throws RPZServiceException, MapcloudException {
        // ARRANGE
        Mapcloud mapcloud = exceptionThrowingMapcloud();
        RPZService sut = new InMemoryRPZService(emptySet(), mapcloud);

        // ACT
        RPZResult actual = sut.rpz("uprn", now().plusDays(1000));

        // ASSERT
        assertFalse(actual.isInRentPressureZone());
    }


    private RPZ rpZForUprn(String uprn) {
        return rpZForUprnAndDataRange(uprn, now().minusDays(100), now().plusDays(100));
    }

    private RPZ rpZForUprnAndDataRange(String uprn, LocalDate from, LocalDate to) {
        return new RPZ("rpz", from, to, 100, emptySet(), singleton(uprn));
    }

    private RPZ rpZForPostcode(String postcode) {
        return rpZForPostcodeAndDataRange(postcode, now().minusDays(100), now().plusDays(100));
    }

    private RPZ rpZForPostcodeAndDataRange(String postcode, LocalDate from, LocalDate to) {
        return new RPZ("rpz", from, to, 100, singleton(postcode), emptySet());
    }

    private Mapcloud anyMapcloud() throws MapcloudException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupPostcode(any())).thenReturn(emptyResults());
        when(mapcloud.lookupUprn(any())).thenReturn(mapcloudResults("", ""));
        return mapcloud;
    }

    private Mapcloud mapcloudWithUprnResults(String uprn, MapcloudResults res) throws MapcloudException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupPostcode(any())).thenReturn(emptyResults());
        when(mapcloud.lookupUprn(eq(uprn))).thenReturn(res);
        return mapcloud;
    }

    private MapcloudResults emptyResults() {
        MapcloudResults results = new MapcloudResults();
        results.setResults(emptyList());
        return results;
    }

    private MapcloudResults mapcloudResults(String uprn, String postcode) {
        MapcloudResults results = new MapcloudResults();
        MapcloudResult result = new MapcloudResult();
        result.setUprn(uprn);
        result.setPostcode(postcode);
        results.setResults(singletonList(result));
        return results;
    }

    private Mapcloud exceptionThrowingMapcloud() throws MapcloudException {
        Mapcloud mapcloud = mock(Mapcloud.class);
        when(mapcloud.lookupUprn(any())).thenThrow(new MapcloudException("", new RuntimeException("")));
        when(mapcloud.lookupPostcode(any())).thenThrow(new MapcloudException("", new RuntimeException("")));
        return mapcloud;
    }
}
