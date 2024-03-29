package scot.mygov.housing.forms.modeltenancy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTenancyResourceTest {

//    @Test
//    public void modelTenancyMultipartJSONVersionReturnsPDFForValidTenancyWithNoTypeParam()
//            throws DocumentGenerationServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.modelTenancyService = mock(DocumentGenerationService.class);
//        sut.modelTenancyValidator = mock(Validator.class);
//        sut.recaptchaCheck = passingRecaptcheCheck();
//        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
//
//        // ACT
//        Response response = sut.modelTenancyMultipart(new ModelTenancy(), "");
//        byte[] bytes = (byte[]) response.getEntity();
//
//        // ASSERT
//        assertEquals(1, bytes[0]);
//        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
//        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
//    }
//
//    @Test
//    public void modelTenancyMultipartReturnsPDFForValidTenancyWithNoTypeParam()
//            throws DocumentGenerationServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.modelTenancyService = mock(DocumentGenerationService.class);
//        sut.modelTenancyValidator = mock(Validator.class);
//        sut.recaptchaCheck = passingRecaptcheCheck();
//        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
//        Map<String, String> params = singletonMap("data", "{}");
//
//        // ACT
//        Response response = sut.modelTenancyMultipart(params);
//        byte[] bytes = (byte[]) response.getEntity();
//
//        // ASSERT
//        assertEquals(1, bytes[0]);
//        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
//        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
//    }
//
//    @Test
//    public void modelTenancyMultipartReturnsDocIfTypeParamIsSetToWord() throws DocumentGenerationServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.modelTenancyService = mock(DocumentGenerationService.class);
//        sut.modelTenancyValidator = mock(Validator.class);
//        sut.recaptchaCheck = passingRecaptcheCheck();
//        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
//        Map<String, String> params = new HashMap<>();
//        params.put("data", "{}");
//        params.put("type", "WORD");
//
//        // ACT
//        Response response = sut.modelTenancyMultipart(params);
//        byte[] bytes = (byte[]) response.getEntity();
//
//        // ASSERT
//        assertEquals(1, bytes[0]);
//        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/docx");
//        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.docx\"");
//    }
//
//    @Test
//    public void modelTenancyMultipartReturnsPDFIfTypeParamIsSetToPDF() throws DocumentGenerationServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.modelTenancyService = mock(DocumentGenerationService.class);
//        sut.modelTenancyValidator = mock(Validator.class);
//        sut.recaptchaCheck = passingRecaptcheCheck();
//        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
//        Map<String, String> params = new HashMap<>();
//        params.put("data", "{}");
//        params.put("type", "PDF");
//
//        // ACT
//        Response response = sut.modelTenancyMultipart(params);
//        byte[] bytes = (byte[]) response.getEntity();
//
//        // ASSERT
//        assertEquals(1, bytes[0]);
//        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
//        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
//    }
//
//    @Test
//    public void modelTenancyMultipartReturnsPDFIfTypeParamIsNoRecognised()
//            throws DocumentGenerationServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.modelTenancyService = mock(DocumentGenerationService.class);
//        sut.modelTenancyValidator = mock(Validator.class);
//        sut.recaptchaCheck = passingRecaptcheCheck();
//        when(sut.modelTenancyService.save(any(), any())).thenReturn(new byte[]{1});
//        Map<String, String> params = new HashMap<>();
//        params.put("data", "{}");
//        params.put("type", "UNRECOGNISED");
//
//        // ACT
//        Response response = sut.modelTenancyMultipart(params);
//        byte[] bytes = (byte[]) response.getEntity();
//
//        // ASSERT
//        assertEquals(1, bytes[0]);
//        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
//        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"tenancy.pdf\"");
//    }
//
//    @Test(expected = DocumentGenerationServiceException.class)
//    public void shouldReturnErrorIfInvalidJSON() throws DocumentGenerationServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        Map<String, String> params = singletonMap("data", "");
//        Response response = sut.modelTenancyMultipart(params);
//        sut.recaptchaCheck = passingRecaptcheCheck();
//
//        // ACT
//        sut.modelTenancyMultipart(params);
//
//        // ASSERT - see expected exception
//    }
//
//    @Test
//    public void failingrecaptcheReturnsClientError() throws DocumentGenerationServiceException {
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.recaptchaCheck = failingRecaptcheCheck();
//
//        // ACT
//        Response response = sut.modelTenancyMultipart(new ModelTenancy(), "");
//
//        // ASSERT
//        assertEquals(response.getStatus(), 400);
//    }

//    @Test
//    public void canGetModelTenancyTemplate() throws ModelTenancyServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.jsonTemplateLoader = new ModelTenancyJsonTemplateLoader();
//
//        // ACT
//        ModelTenancy modelTenancy = sut.modelTenancyTemplate();
//
//        // ASSERT
//        Assert.assertNotNull(modelTenancy);
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getContentsAndConditions()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getLocalAuthorityTaxesAndCharges()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getAlterations()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getPrivateGarden()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getRoof()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getBinsAndRecycling()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getStorage()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getDangerousSubstances()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getPets()));
//        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getSmoking()));
//    }
//
//    @Test(expected = ModelTenancyServiceException.class)
//    public void templateLoaderExceptionIsWrappedAsServiceException() throws ModelTenancyServiceException {
//
//        // ARRANGE
//        ModelTenancyResource sut = new ModelTenancyResource();
//        sut.jsonTemplateLoader = exceptionThrowingTemplateLoader();
//
//        // ACT
//        ModelTenancy actual = sut.modelTenancyTemplate();
//
//        // ASSERT
//        Assert.assertNotNull(actual);
//    }

    private ModelTenancyJsonTemplateLoader exceptionThrowingTemplateLoader() {
        ModelTenancyJsonTemplateLoader o = mock(ModelTenancyJsonTemplateLoader.class);
        when(o.loadJsonTemplate()).thenThrow(new RuntimeException("somehting went wrong"));
        return o;
    }

}
