package scot.mygov.housing.forms.modeltenancy;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.Validator;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTenancyResourceTest {

    @Test
    public void modelTenancyMultipartJSONVersionReturnsPDFForValidTenancyWithNoTypeParam()
            throws ModelTenancyServiceException, DocumentGeneratorException {

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
    public void modelTenancyMultipartReturnsPDFForValidTenancyWithNoTypeParam()
            throws ModelTenancyServiceException, DocumentGeneratorException {
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
    public void modelTenancyMultipartReturnsDocIfTypeParamIsSetToWord()
            throws ModelTenancyServiceException, DocumentGeneratorException {
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
    public void modelTenancyMultipartReturnsPDFIfTypeParamIsSetToPDF()
            throws ModelTenancyServiceException, DocumentGeneratorException {
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
    public void modelTenancyMultipartReturnsPDFIfTypeParamIsNoRecognised()
            throws ModelTenancyServiceException, DocumentGeneratorException {
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
    public void shouldReturnErrorIfInvalidJSON()
            throws ModelTenancyServiceException, DocumentGeneratorException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        Map<String, String> params = singletonMap("data", "");
        Response response = sut.modelTenancyMultipart(params);

        // ACT
        sut.modelTenancyMultipart(params);

        // ASSERT - see expected exception
    }

    @Test
    public void canGetModelTenancyTemplate() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        sut.jsonTemplateLoader = new ModelTenancyJsonTemplateLoader();

        // ACT
        ModelTenancy modelTenancy = sut.modelTenancyTemplate();

        // ASSERT
        Assert.assertNotNull(modelTenancy);
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getContentsAndConditions()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getLocalAuthorityTaxesAndCharges()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getAlterations()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getPrivateGarden()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getRoof()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getBinsAndRecycling()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getStorage()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getDangerousSubstances()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getPets()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getSmoking()));
    }

    @Test(expected = ModelTenancyServiceException.class)
    public void templateLoaderExceptionIsWrappedAsServiceException() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyResource sut = new ModelTenancyResource();
        sut.jsonTemplateLoader = exceptionThrowingTemplateLoader();

        // ACT
        ModelTenancy actual = sut.modelTenancyTemplate();

        // ASSERT
        Assert.assertNotNull(actual);
    }

    private ModelTenancyJsonTemplateLoader exceptionThrowingTemplateLoader() {
        ModelTenancyJsonTemplateLoader o = mock(ModelTenancyJsonTemplateLoader.class);
        when(o.loadJsonTemplate()).thenThrow(new RuntimeException("somehting went wrong"));
        return o;
    }
}
