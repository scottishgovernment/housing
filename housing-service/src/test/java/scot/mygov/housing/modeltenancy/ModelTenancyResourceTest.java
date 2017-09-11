package scot.mygov.housing.modeltenancy;

import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.Validator;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTenancyResourceTest {

    @Test
    public void templateReturnsModelTenancyObject() throws Exception {
        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        ModelTenancy expected = new ModelTenancy();
        sut.modelTenancyService = mock(ModelTenancyService.class);
        when(sut.modelTenancyService.getModelTenancyTemplate()).thenReturn(expected);

        // ACT
        ModelTenancy actual = sut.modelTenancyTemplate();

        // ASSERT
        assertSame(expected, actual);
        assertNotEquals("model is null", actual);

    }

    @Test
    public void modelTenancyMultipartJSONVersionReturnsPDFForValidTenancyWithNoTypeParam() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        sut.modelTenancyService = mock(ModelTenancyService.class);
        sut.modelTenancyValidator = mock(Validator.class);
        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});


        // ACT
        Response response = sut.modelTenancyMultipart(new ModelTenancy(), "");
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
    }

    @Test
    public void modelTenancyMultipartReturnsPDFForValidTenancyWithNoTypeParam() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        sut.modelTenancyService = mock(ModelTenancyService.class);
        sut.modelTenancyValidator = mock(Validator.class);
        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = singletonMap("data", "{}");

        // ACT
        Response response = sut.modelTenancyMultipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
    }

    @Test
    public void modelTenancyMultipartReturnsDocIfTypeParamIsSetToWord() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        sut.modelTenancyService = mock(ModelTenancyService.class);
        sut.modelTenancyValidator = mock(Validator.class);
        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = new HashMap<>();
        params.put("data", "{}");
        params.put("type", "WORD");

        // ACT
        Response response = sut.modelTenancyMultipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/docx");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.docx\"");
    }

    @Test
    public void modelTenancyMultipartReturnsPDFIfTypeParamIsSetToPDF() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        sut.modelTenancyService = mock(ModelTenancyService.class);
        sut.modelTenancyValidator = mock(Validator.class);
        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = new HashMap<>();
        params.put("data", "{}");
        params.put("type", "PDF");

        // ACT
        Response response = sut.modelTenancyMultipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
    }

    @Test
    public void modelTenancyMultipartReturnsPDFIfTypeParamIsNoRecognised() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        sut.modelTenancyService = mock(ModelTenancyService.class);
        sut.modelTenancyValidator = mock(Validator.class);
        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = new HashMap<>();
        params.put("data", "{}");
        params.put("type", "UNRECOGNISED");

        // ACT
        Response response = sut.modelTenancyMultipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
    }

    @Test(expected = ModelTenancyServiceException.class)
    public void shouldReturnErrorIfInvalidJSON() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        Map<String, String> params = singletonMap("data", "");
        Response response = sut.modelTenancyMultipart(params);

        // ACT
        sut.modelTenancyMultipart(params);

        // ASSERT - see expected exception
    }


}
