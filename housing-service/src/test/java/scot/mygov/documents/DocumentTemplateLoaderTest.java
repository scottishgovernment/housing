package scot.mygov.documents;

import com.aspose.words.Document;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTemplateLoaderTest {

    @Test
    public void canLoadTemplate() {
        DocumentTemplateLoader loader = new DocumentTemplateLoader(ModelTenancyService.DOCUMENT_TEMPLATE_PATH, null);
        Document doc1 = loader.loadDocumentTemplate();
        assertNotNull(doc1);

        // load again, returns a clone.
        Document doc2 = loader.loadDocumentTemplate();
        assertNotEquals(doc1, doc2);
    }

    // exception thrown for non existant template
    @Test(expected = RuntimeException.class)
    public void expectedExceptionForNonExsitantPath() throws RuntimeException {
        new DocumentTemplateLoader("nonexistanttemplate", null).loadDocumentTemplate();
    }

}
