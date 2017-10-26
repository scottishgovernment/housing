package scot.mygov.housing.forms.modeltenancy;

import com.aspose.words.Document;
import com.codahale.metrics.MetricRegistry;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.AsposeLicense;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyObjectMother;

import java.io.ByteArrayInputStream;


public class ModelTenancyServiceTest {

    private final ModelTenancyObjectMother om = new ModelTenancyObjectMother();

    private final HousingConfiguration config = new HousingConfiguration();

    private final ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();

    @Test
    public void canGeneratePDF() throws Exception {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, new MetricRegistry());

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
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, new MetricRegistry());

        // ACT
        ModelTenancy modelTenancy = om.anyTenancy();
        byte [] result = sut.save(modelTenancy, DocumentType.WORD);

        // ASSERT - can parse it as a pdf
        Document document = new Document(new ByteArrayInputStream(result));
    }

    private DocumentTemplateLoader templateLoader() {
        return new DocumentTemplateLoader(ModelTenancyService.DOCUMENT_TEMPLATE_PATH, new AsposeLicense(null));
    }

}
