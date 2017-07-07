package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import org.junit.Test;
import scot.mygov.housing.AsposeLicense;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class ModelTenancyDocumentTemplateLoaderTest {

    @Test
    public void canLoadTemplate() throws TemplateLoaderException {
        ModelTenancyDocumentTemplateLoader loader = new ModelTenancyDocumentTemplateLoader(new AsposeLicense(null));
        Document doc1 = loader.loadDocumentTemplate();
        assertNotNull(doc1);

        // load again, returns a clone.
        Document doc2 = loader.loadDocumentTemplate();
        assertNotEquals(doc1, doc2);
    }

    // exception thrown for non existant template
    @Test(expected = RuntimeException.class)
    public void expectedExceptionForNonExsitantPath() throws TemplateLoaderException {
        new ModelTenancyDocumentTemplateLoader("nonexistanttemplate").loadDocumentTemplate();
    }

}
