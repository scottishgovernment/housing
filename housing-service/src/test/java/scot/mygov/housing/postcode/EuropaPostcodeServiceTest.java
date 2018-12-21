package scot.mygov.housing.postcode;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import scot.mygov.housing.europa.AddressResultWrapper;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.europa.EuropaAddress;
import scot.mygov.housing.europa.EuropaException;
import scot.mygov.housing.europa.EuropaResults;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EuropaPostcodeServiceTest {
    @Test(expected = PostcodeServiceException.class)
    public void mapcloudExceptionHandledCorrectly() throws PostcodeServiceException, EuropaException {

        // ARRANGE
        PostcodeService sut = new EuropaPostcodeService(excpetionThrowingMapcloud());

        // ACT
        sut.lookup(scottishPostcode());

        // ASSERT -- see expected exception
    }

    @Test
    public void greenpathResultsParsedCorrectly() throws PostcodeServiceException, EuropaException {

        // ARRANGE
        EuropaResults results = greenpathMapcloudResults();
        PostcodeService sut = new EuropaPostcodeService(mapcloudWithResults(results));

        // ACT
        PostcodeServiceResults actual = sut.lookup(scottishPostcode());

        // ASSERT
        assertFalse(actual.getResults().isEmpty());
    }

    @Test
    public void formatsBuilding() {
        EuropaAddress address = new EuropaAddress();
        address.setBuildingName("Something House");
        EuropaPostcodeService sut = new EuropaPostcodeService(null);
        String actual = sut.building(address);
        assertEquals("Something House", actual);
    }

    @Test
    public void poBoxAddedToBuilding() {
        EuropaAddress address = new EuropaAddress();
        address.setPobox("111");
        address.setBuildingNumber("999");
        address.setSubBuildingName("Annex 1");
        address.setBuildingName("Something House");
        EuropaPostcodeService sut = new EuropaPostcodeService(null);
        String actual = sut.building(address);
        assertEquals("PO Box 111 999 Annex 1 Something House", actual);
    }

    private String scottishPostcode() {
        return "EH10 4AX";
    }

    private Europa excpetionThrowingMapcloud() throws EuropaException {
        Europa mapcloud = mock(Europa.class);
        when(mapcloud.lookupPostcode(any())).thenThrow(new EuropaException("arg", new RuntimeException("arg")));
        return mapcloud;
    }

    private Europa mapcloudWithResults(EuropaResults results) throws EuropaException {
        Europa mapcloud = mock(Europa.class);
        when(mapcloud.lookupPostcode(any())).thenReturn(results);
        return mapcloud;
    }

    private EuropaResults greenpathMapcloudResults() {
        EuropaResults res = new EuropaResults();
        AddressResultWrapper wrapper = new AddressResultWrapper();
        res.getResults().add(wrapper);
        EuropaAddress one = anyResult();
        EuropaAddress two = anyResult();
        EuropaAddress three = anyResult();
        EuropaAddress four = anyResult();
        one.setThoroughfare("111 Some street");
        two.setThoroughfare("11 Some street");
        three.setThoroughfare("1 Some street");
        four.setThoroughfare("Some street");
        wrapper.getAddress().add(one);
        wrapper.getAddress().add(two);
        wrapper.getAddress().add(three);
        wrapper.getAddress().add(four);
        return res;
    }

    private EuropaAddress anyResult() {
        EuropaAddress result = new EuropaAddress();
        result.setUprn(RandomStringUtils.random(6));
        result.setPostcode("EH10 4AX");
        result.setDepartmentName("deptname");
        result.setOrganisationName("orgname");
        result.setBuildingName("buildingname");
        result.setBuildingNumber("1");
        result.setPobox("1");
        result.setDependentThoroughfare("dependentThoroughfare");
        result.setThoroughfare("thoroughfare");
        result.setDoubleDependentLocality("setDoubleDependentLocality");
        result.setDependentLocality("setDependentLocality");
        result.setTown("town");
        return result;
    }
}