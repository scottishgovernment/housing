package scot.mygov.documents;

import com.aspose.words.Document;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentGeneratorTest {

    @Test
    public void canGeneratePDF() throws Exception {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        DocumentGenerator sut = new DocumentGenerator(templateLoader);

        // ACT
        byte[] result = sut.save(fieldMap(), DocumentType.PDF);

        // ASSERT - can parse it as a pdf
        PDDocument document = Loader.loadPDF(result);
        document.close();
    }


    @Test
    public void canGenerateWordDocument() throws Exception {

        // ARRANGE
        DocumentTemplateLoader templateLoader = templateLoader();
        DocumentGenerator sut = new DocumentGenerator(templateLoader);

        // ACT
        byte[] result = sut.save(fieldMap(), DocumentType.WORD);

        // ASSERT
        new Document(new ByteArrayInputStream(result));
    }

    @Test(expected = RuntimeException.class)
    public void loaderThrowsExceptionOnSave() throws DocumentGeneratorException {

        DocumentTemplateLoader templateLoader = exceptionThrowingTemplateLoader();
        DocumentGenerator sut = new DocumentGenerator(templateLoader);
        sut.save(fieldMap(), DocumentType.PDF);
    }

    private DocumentTemplateLoader templateLoader() {
        return new DocumentTemplateLoaderBasicImpl("/templates/model-tenancy-agreement.docx", null);
    }

    private DocumentTemplateLoader exceptionThrowingTemplateLoader() {
        DocumentTemplateLoader loader = mock(DocumentTemplateLoader.class);
        when(loader.loadDocumentTemplate()).thenThrow(new RuntimeException("Arg", null));
        return loader;
    }

    private Map<String, Object> fieldMap() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("field1", "field1value");
        return fields;
    }
}
