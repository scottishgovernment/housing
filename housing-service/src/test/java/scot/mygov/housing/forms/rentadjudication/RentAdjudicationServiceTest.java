package scot.mygov.housing.forms.rentadjudication;

import com.aspose.words.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.AsposeLicense;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;

import java.io.ByteArrayInputStream;

public class RentAdjudicationServiceTest {
    private final RentAdjudicationObjectMother om = new RentAdjudicationObjectMother();

    private final HousingConfiguration config = new HousingConfiguration();

    private final RentAdjudicationFieldExtractor fieldExtractor = new RentAdjudicationFieldExtractor();

    @Test
    public void canGeneratePDF() throws Exception {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        RentAdjudicationService sut = new RentAdjudicationService(templateLoader, fieldExtractor);

        // ACT
        RentAdjudication input = om.anyRentAdjudication();
        byte [] result = sut.save(input, DocumentType.PDF);

        // ASSERT - can parse it as a pdf
        PDDocument document = PDDocument.load(new ByteArrayInputStream(result));
        document.close();
    }

    @Test
    public void canGenerateWordDocument() throws Exception {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        RentAdjudicationService sut = new RentAdjudicationService(templateLoader, fieldExtractor);

        // ACT
        RentAdjudication modelTenancy = om.anyRentAdjudication();
        byte [] result = sut.save(modelTenancy, DocumentType.WORD);

        // ASSERT - can parse it as a pdf
        Document document = new Document(new ByteArrayInputStream(result));
    }

    @Test(expected = RentAdjudicationServiceException.class)
    public void documentGeneratorExceptionWrapped() throws RentAdjudicationServiceException, DocumentGeneratorException {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        RentAdjudicationService sut = new RentAdjudicationService(templateLoader, fieldExtractor);
        sut.documentGenerator = exceptionThrowingDocGenerator();

        // ACT
        RentAdjudication modelTenancy = om.anyRentAdjudication();
        byte [] result = sut.save(modelTenancy, DocumentType.WORD);

        // ASSERT - see expected
    }


    private DocumentTemplateLoader templateLoader() {
        return new DocumentTemplateLoader(RentAdjudicationService.DOCUMENT_TEMPLATE_PATH, new AsposeLicense(null));
    }

    private DocumentGenerator exceptionThrowingDocGenerator() throws DocumentGeneratorException {
        DocumentGenerator dg = Mockito.mock(DocumentGenerator.class);
        Mockito.when(dg.save(Mockito.any(), Mockito.any())).thenThrow(new DocumentGeneratorException("", new RuntimeException()));
        return dg;
    }
}
