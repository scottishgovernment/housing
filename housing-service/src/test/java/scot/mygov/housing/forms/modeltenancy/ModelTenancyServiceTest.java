package scot.mygov.housing.forms.modeltenancy;

import com.aspose.words.Document;
import com.codahale.metrics.MetricRegistry;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.AsposeLicense;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyObjectMother;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ModelTenancyServiceTest {

    private final ModelTenancyObjectMother om = new ModelTenancyObjectMother();

    private final HousingConfiguration config = new HousingConfiguration();

    private final ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();

    @Test
    public void canGeneratePDF() throws Exception {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
        ModelTenancyService sut = new ModelTenancyService(documentGenerator, fieldExtractor, new MetricRegistry());

        // ACT
        ModelTenancy modelTenancy = om.anyTenancy();
        byte [] result = sut.save(modelTenancy, DocumentType.PDF);

        // ASSERT - can parse it as a pdf
        PDDocument document = PDDocument.load(new ByteArrayInputStream(result));
        document.close();
    }

    @Test
    public void canGenerateWordDocument() throws Exception {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
        ModelTenancyService sut = new ModelTenancyService(documentGenerator, fieldExtractor, new MetricRegistry());

        // ACT
        ModelTenancy modelTenancy = om.anyTenancy();
        byte [] result = sut.save(modelTenancy, DocumentType.WORD);

        // ASSERT - can parse it as a pdf
        Document document = new Document(new ByteArrayInputStream(result));
    }

    @Test(expected = ModelTenancyServiceException.class)
    public void documentGeneratorExceptionWrappedCorrectly() throws Exception {

        // ARRANGE
        DocumentGenerator documentGenerator = exceptionThrowingGenerator();
        ModelTenancyService sut = new ModelTenancyService(documentGenerator, fieldExtractor, new MetricRegistry());

        // ACT
        ModelTenancy modelTenancy = om.anyTenancy();
        byte [] result = sut.save(modelTenancy, DocumentType.WORD);

        // ASSERT - see expected
    }

    private DocumentTemplateLoader templateLoader() {
        return new DocumentTemplateLoader(ModelTenancyService.DOCUMENT_TEMPLATE_PATH, new AsposeLicense(null));
    }

    private DocumentGenerator exceptionThrowingGenerator() throws DocumentGeneratorException {
        DocumentGenerator documentGenerator = mock(DocumentGenerator.class);
        when(documentGenerator.save(any(), any(), any()))
                .thenThrow(new DocumentGeneratorException("", new RuntimeException("arg!")));
        return documentGenerator;
    }
}
