package scot.mygov.housing.europa;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EuropaTest {

    @Test
    public void greenpathPostcode() throws Exception {

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
    public void greenpathUprn() throws Exception {

        // ARRANGE
        EuropaResults results = uprnResults("anyuprn");
        Europa sut = new Europa(targetWithResults(results), new MetricRegistry());

        // ACT
        EuropaResults actual = sut.lookupUprn("uprn");

        // ASSERT
        assertEquals(actual.getResults().size(), 1);
        assertEquals(actual.getResults().get(0).getAddress().get(0).getUprn(), "anyuprn");

    }

    @Test
    public void emptyResultsHandledCorrectly() throws Exception {
        // ARRANGE
        EuropaResults results = uprnResults("anyuprn");
        results.getMetadata().setCount(0);
        results.setResults(Collections.emptyList());
        Europa sut = new Europa(targetWithResults(results), new MetricRegistry());

        // ACT
        EuropaResults actual = sut.lookupUprn("uprn");

        // ASSERT
        assertEquals(actual.getResults().size(), 0);
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
        sut.lookupUprn("uprn");

        // ASSERT -- see expected exception
    }

    private EuropaResults uprnResults(String uprn) {
        EuropaResults results = new EuropaResults();
        results.setMetadata(new EuropaMetadata());
        results.getMetadata().setCount(1);
        AddressResultWrapper wrapper = new AddressResultWrapper();
        wrapper.getAddress().add(uprnResult(uprn));
        results.getResults().add(wrapper);
        return results;

    }

    private EuropaResults postcodeResults(String postcode) {
        EuropaResults results = new EuropaResults();
        results.setMetadata(new EuropaMetadata());
        results.getMetadata().setCount(1);
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

    private WebTarget targetWithResults(EuropaResults results) throws Exception {
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

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(results);
        when(builder.get(JsonNode.class)).thenReturn(objectMapper.readValue(jsonString, JsonNode.class));

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
        when(builder.get(JsonNode.class)).thenThrow(t);
        return target;
    }
}
