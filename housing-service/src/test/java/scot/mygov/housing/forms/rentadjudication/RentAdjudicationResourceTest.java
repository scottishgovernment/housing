package scot.mygov.housing.forms.rentadjudication;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RentAdjudicationResourceTest {
    @Test
    public void multipartJSONVersionReturnsPDFForValidTenancyWithNoTypeParam()
            throws RentAdjudicationServiceException, DocumentGeneratorException {

        // ARRANGE
        RentAdjudicationResource sut = new RentAdjudicationResource();
        sut.service = mock(RentAdjudicationService.class);
        when(sut.service.save(any(), any())).thenReturn(new byte[]{1});

        // ACT
        Response response = sut.multipart(new RentAdjudication(), "");
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"adjudication.pdf\"");
    }

    @Test
    public void multipartReturnsPDFForValidTenancyWithNoTypeParam()
            throws RentAdjudicationServiceException, DocumentGeneratorException {
        // ARRANGE
        RentAdjudicationResource sut = new RentAdjudicationResource();
        sut.service = mock(RentAdjudicationService.class);
        when(sut.service.save(any(), any())).thenReturn(new byte[]{1});
        Map<String, String> params = singletonMap("data", "{}");

        // ACT
        Response response = sut.multipart(params);
        byte[] bytes = (byte[]) response.getEntity();

        // ASSERT
        assertEquals(1, bytes[0]);
        assertEquals(response.getHeaders().getFirst("Content-Type"), "application/pdf");
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"adjudication.pdf\"");
    }

    @Test
    public void multipartReturnsDocIfTypeParamIsSetToWord()
            throws RentAdjudicationServiceException, DocumentGeneratorException {
        // ARRANGE
        RentAdjudicationResource sut = new RentAdjudicationResource();
        sut.service = mock(RentAdjudicationService.class);
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
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"adjudication.docx\"");
    }

    @Test
    public void multipartReturnsPDFIfTypeParamIsSetToPDF()
            throws RentAdjudicationServiceException, DocumentGeneratorException {
        // ARRANGE
        RentAdjudicationResource sut = new RentAdjudicationResource();
        sut.service = mock(RentAdjudicationService.class);
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
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"adjudication.pdf\"");
    }

    @Test
    public void modelTenancyMultipartReturnsPDFIfTypeParamIsNoRecognised()
            throws RentAdjudicationServiceException, DocumentGeneratorException {
        // ARRANGE
        RentAdjudicationResource sut = new RentAdjudicationResource();
        sut.service = mock(RentAdjudicationService.class);

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
        assertEquals(response.getHeaders().getFirst("Content-Disposition"), "attachment; filename=\"adjudication.pdf\"");
    }

    @Test(expected = RentAdjudicationServiceException.class)
    public void shouldReturnErrorIfInvalidJSON()
            throws RentAdjudicationServiceException, DocumentGeneratorException {

        // ARRANGE
        RentAdjudicationResource sut = new RentAdjudicationResource();
        Map<String, String> params = singletonMap("data", "");
        Response response = sut.multipart(params);

        // ACT
        sut.multipart(params);

        // ASSERT - see expected exception
    }
}
