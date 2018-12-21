package scot.mygov.housing.europa;

import com.codahale.metrics.MetricRegistry;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EuropaTest {

    @Test
    public void greenpathPostcode() throws EuropaException {

        // ARRANGE
        EuropaResults results = postcodeResults("EH10 4AX");
        Europa sut = new Europa(targetWithResults(results), new MetricRegistry());

        // ACT
        EuropaResults actual = sut.lookupPostcode("uprn");

        // ASSERT
        assertEquals(actual.getResults().size(), results.getResults().size());
        assertEquals(actual.getResults().get(0).getAddress().get(0).getPostcode(), "EH10 4AX");
    }

    @Test
    public void greenpathUprn() throws EuropaException {

        // ARRANGE
        EuropaResults results = uprnResults("anyuprn");
        Europa sut = new Europa(targetWithResults(results), new MetricRegistry());

        // ACT
        EuropaResults actual = sut.lookupUprn("uprn");

        // ASSERT
        assertEquals(actual.getResults().size(), 1);
        assertEquals(actual.getResults().get(0).getAddress().get(0).getUprn(), "anyuprn");

    }

    @Test(expected=EuropaException.class)
    public void processingExceptionWrappedAsExpected() throws EuropaException {
        // ARRANGE
        Europa sut = new Europa(exceptionThrowingTarget(new ProcessingException("")), new MetricRegistry());

        // ACT
        sut.lookupUprn("uprn");

        // ASSERT -- see expected exception
    }

    @Test(expected=EuropaException.class)
    public void webApplicationExceptionWrappedAsExpected() throws EuropaException {
        // ARRANGE
        Europa sut = new Europa(exceptionThrowingTarget(new ProcessingException("")), new MetricRegistry());

        // ACT
        EuropaResults actual = sut.lookupUprn("uprn");

        // ASSERT -- see expected exception
    }

    private EuropaResults uprnResults(String uprn) {
        EuropaResults results = new EuropaResults();
        AddressResultWrapper wrapper = new AddressResultWrapper();
        wrapper.getAddress().add(uprnResult(uprn));
        results.getResults().add(wrapper);
        return results;

    }

    private EuropaResults postcodeResults(String postcode) {
        EuropaResults results = new EuropaResults();
        AddressResultWrapper wrapper = new AddressResultWrapper();
        wrapper.getAddress().add(postcodeResult(postcode));
        results.getResults().add(wrapper);
        return results;
    }

    private EuropaAddress uprnResult(String uprn) {
        EuropaAddress result = new EuropaAddress();
        result.setUprn(uprn);
        result.setPostcode("EH104AX");
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

    private EuropaAddress postcodeResult(String postcode) {
        EuropaAddress result = new EuropaAddress();
        result.setUprn(RandomStringUtils.random(6));
        result.setPostcode(postcode);
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

    private WebTarget targetWithResults(EuropaResults results) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(eq("uprn"), any())).thenReturn(target);
        when(target.queryParam(eq("postcode"), any())).thenReturn(target);
        when(target.queryParam(eq("fieldset"), any())).thenReturn(target);
        when(target.queryParam(eq("addresstype"), eq("dpa"))).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(200);
        when(builder.header(any(), any())).thenReturn(builder);
        when(builder.get(EuropaResults.class)).thenReturn(results);
        return target;
    }


    private WebTarget exceptionThrowingTarget(Throwable t) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(target.path(any())).thenReturn(target);
        when(target.queryParam(eq("uprn"), any())).thenReturn(target);
        when(target.queryParam(eq("postcode"), any())).thenReturn(target);
        when(target.queryParam(eq("fieldset"), any())).thenReturn(target);
        when(target.queryParam(eq("addresstype"), eq("dpa"))).thenReturn(target);
        when(target.request()).thenReturn(builder);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(200);
        when(builder.header(any(), any())).thenReturn(builder);
        when(builder.get(EuropaResults.class)).thenThrow(t);
        return target;
    }
}
