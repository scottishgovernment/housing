package scot.mygov.housing.modeltenancy;

import org.junit.Test;
import scot.mygov.validation.Validator;

import javax.ws.rs.core.Response;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTenancyResourceTest {

    @Test
    public void returnsPDFIfModelIsValid() throws ModelTenancyServiceException {
        ModelTenancyResource resource = new ModelTenancyResource();
        resource.modelTenancyService = mock(ModelTenancyService.class);
        resource.modelTenancyValidator = mock(Validator.class);
        when(resource.modelTenancyService.save(any())).thenReturn(new byte[]{1});
        Map<String, String> params = singletonMap("data", "{}");
        Response response = resource.modelTenancyMultipart(params);
        byte[] bytes = (byte[]) response.getEntity();
        assertEquals(1, bytes[0]);
    }

    @Test(expected = ModelTenancyServiceException.class)
    public void shouldReturnErrorIfInvalidJSON() throws ModelTenancyServiceException {
        ModelTenancyResource resource = new ModelTenancyResource();
        Map<String, String> params = singletonMap("data", "");
        Response response = resource.modelTenancyMultipart(params);
        resource.modelTenancyMultipart(params);
    }

}
