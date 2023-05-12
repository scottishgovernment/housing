package scot.mygov.housing.forms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractDocumentGenerationResourceTest {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DummyFormData extends AbstractFormModel {
        private String name = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class DummyDocumentationGenarationResource extends AbstractDocumentGenerationResource<DummyFormData> {

        public DummyDocumentationGenarationResource(
                DocumentGenerationService<DummyFormData> service,
                RecaptchaCheck recaptchaCheck) {
            super(service, recaptchaCheck);
        }

        protected String contentDispositionFilenameStem() {
            return "dummy";
        }

        protected Class<DummyFormData> getModelClass() {
            return DummyFormData.class;
        }
    }


    @Test
    public void multipartJSONVersionReturnsPDFForValidTenancyWithNoTypeParam()
            throws DocumentGenerationServiceException {

        // ARRANGE
        DummyDocumentationGenarationResource sut
                = new DummyDocumentationGenarationResource(
                        service(new byte[]{1}),
                        passingRecaptchaCheck());

        // ACT
        Response response = sut.multipart(exampleFormData(), "");
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"dummy.pdf\"");
    }


    @Test
    public void multipartReturnsPDFForValidTenancyWithNoTypeParam() throws Exception {

        // ARRANGE
        Map<String, String> params = Collections.singletonMap("data", exampleFormDataString());

        DummyDocumentationGenarationResource sut
                = new DummyDocumentationGenarationResource(
                service(new byte[]{1}),
                passingRecaptchaCheck());

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"dummy.pdf\"");
    }

    @Test
    public void multipartReturnsDocIfTypeParamIsSetToWord() throws Exception {
        // ARRANGE
        Map<String, String> params = new HashMap<>();
        params.put("data", exampleFormDataString());
        params.put("type", "WORD");

        DummyDocumentationGenarationResource sut
                = new DummyDocumentationGenarationResource(
                service(new byte[]{1}),
                passingRecaptchaCheck());

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/docx");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"dummy.docx\"");
    }

    @Test
    public void multipartReturnsPDFIfTypeParamIsSetToPDF() throws Exception {
        // ARRANGE
        Map<String, String> params = new HashMap<>();
        params.put("data", exampleFormDataString());
        params.put("type", "PDF");

        DummyDocumentationGenarationResource sut
                = new DummyDocumentationGenarationResource(
                service(new byte[]{1}),
                passingRecaptchaCheck());

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"dummy.pdf\"");
    }

    @Test
    public void multipartReturnsPDFIfTypeParamIsNoRecognised() throws Exception {

        // ARRANGE
        Map<String, String> params = new HashMap<>();
        params.put("data", exampleFormDataString());
        params.put("type", "UNRECOGNISED");

        DummyDocumentationGenarationResource sut
                = new DummyDocumentationGenarationResource(
                service(new byte[]{1}),
                passingRecaptchaCheck());

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"dummy.pdf\"");
    }

    @Test(expected = DocumentGenerationServiceException.class)
    public void shouldReturnErrorIfInvalidJSON() throws DocumentGenerationServiceException {

        // ARRANGE
        Map<String, String> params = new HashMap<>();
        params.put("data", "");

        DummyDocumentationGenarationResource sut
                = new DummyDocumentationGenarationResource(
                service(new byte[]{1}),
                passingRecaptchaCheck());

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT - see expected exception
    }

    @Test
    public void failingrecaptcheReturnsClientError() throws Exception {
        // ARRANGE
        Map<String, String> params = new HashMap<>();
        params.put("data", exampleFormDataString());
        params.put("type", "UNRECOGNISED");

        DummyDocumentationGenarationResource sut
                = new DummyDocumentationGenarationResource(
                service(new byte[]{1}),
                failingRecaptchaCheck());

        // ACT
        Response response = sut.multipart(params);

        // ASSERT
        assertEquals(response.getStatus(), 400);
    }

    private RecaptchaCheck passingRecaptchaCheck() {
        RecaptchaCheck recaptchaCheck = mock(RecaptchaCheck.class);
        when(recaptchaCheck.verify(any())).thenReturn(true);
        return recaptchaCheck;
    }

    private RecaptchaCheck failingRecaptchaCheck() {
        RecaptchaCheck recaptchaCheck = mock(RecaptchaCheck.class);
        when(recaptchaCheck.verify(any())).thenReturn(false);
        return recaptchaCheck;
    }

    private DocumentGenerationService<DummyFormData> service(byte[] bytes) throws DocumentGenerationServiceException {
        DocumentGenerationService<DummyFormData> service = mock(DocumentGenerationService.class);
        when(service.save(any(), any())).thenReturn(bytes);
        return service;
    }

    String exampleFormDataString() throws Exception {
        return new ObjectMapper().writeValueAsString(exampleFormData());
    }

    DummyFormData exampleFormData() {
        DummyFormData data = new DummyFormData();
        return data;
    }
}
