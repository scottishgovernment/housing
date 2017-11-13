package scot.mygov.housing.forms.nonprovisionofdocumentation;

import org.junit.Test;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.DocumentGenerationServiceException;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NonProvisionOfDocumentationResourceTest {

    @Test
    public void multipartJSONVersionReturnsPDFForValidTenancyWithNoTypeParam()
            throws DocumentGenerationServiceException {

        // ARRANGE
        NonProvisionOfDocumentationResource sut = new NonProvisionOfDocumentationResource();
        sut.service = mock(DocumentGenerationService.class);
        when(sut.service.save(any(), any())).thenReturn(new byte[]{1});

        // ACT
        Response response = sut.multipart(new NonProvisionOfDocumentation(), "");
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"non-provision-of-documentation.pdf\"");
    }

    @Test
    public void multipartReturnsPDFForValidTenancyWithNoTypeParam()
            throws DocumentGenerationServiceException {
        // ARRANGE
        NonProvisionOfDocumentationResource sut = new NonProvisionOfDocumentationResource();
        sut.service = mock(DocumentGenerationService.class);
        when(sut.service.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = singletonMap("data", "{}");

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"non-provision-of-documentation.pdf\"");
    }

    @Test
    public void multipartReturnsDocIfTypeParamIsSetToWord()
            throws DocumentGenerationServiceException {
        // ARRANGE
        NonProvisionOfDocumentationResource sut = new NonProvisionOfDocumentationResource();
        sut.service = mock(DocumentGenerationService.class);
        when(sut.service.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = new HashMap<>();
        params.put("data", "{}");
        params.put("type", "WORD");

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/docx");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"non-provision-of-documentation.docx\"");
    }

    @Test
    public void multipartReturnsPDFIfTypeParamIsSetToPDF()
            throws DocumentGenerationServiceException {
        // ARRANGE
        NonProvisionOfDocumentationResource sut = new NonProvisionOfDocumentationResource();
        sut.service = mock(DocumentGenerationService.class);
        when(sut.service.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = new HashMap<>();
        params.put("data", "{}");
        params.put("type", "PDF");

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"non-provision-of-documentation.pdf\"");
    }

    @Test
    public void modelTenancyMultipartReturnsPDFIfTypeParamIsNoRecognised()
            throws DocumentGenerationServiceException {
        // ARRANGE
        NonProvisionOfDocumentationResource sut = new NonProvisionOfDocumentationResource();
        sut.service = mock(DocumentGenerationService.class);

        when(sut.service.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = new HashMap<>();
        params.put("data", "{}");
        params.put("type", "UNRECOGNISED");

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"non-provision-of-documentation.pdf\"");
    }

    @Test(expected = DocumentGenerationServiceException.class)
    public void shouldReturnErrorIfInvalidJSON()
            throws DocumentGenerationServiceException {

        // ARRANGE
        NonProvisionOfDocumentationResource sut = new NonProvisionOfDocumentationResource();
        Map<String, String> params = singletonMap("data", "");
        Response response = sut.multipart(params);

        // ACT
        sut.multipart(params);

        // ASSERT - see expected exception
    }
}
