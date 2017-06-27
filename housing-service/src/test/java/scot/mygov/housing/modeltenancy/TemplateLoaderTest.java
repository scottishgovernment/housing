package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.HousingConfiguration;

/**
 * Created by z418868 on 21/06/2017.
 */
public class TemplateLoaderTest {

    @Test
    public void canLoadTemplate() throws TemplateLoaderException {
        ModelTenancyDocumentTemplateLoader loader = new ModelTenancyDocumentTemplateLoader(new HousingConfiguration().getModelTenancyTemplatePath());
        Document doc1 = loader.loadDocumentTemplate();
        Assert.assertNotNull(doc1);

        // load again, returns a clone.
        Document doc2 = loader.loadDocumentTemplate();
        Assert.assertNotEquals(doc1, doc2);
    }


    // exception throwsn for non existant template
    @Test(expected = RuntimeException.class)
    public void expectedExceptionForNonExsitantPath() throws TemplateLoaderException {
        new ModelTenancyDocumentTemplateLoader("nonexistanttemplate").loadDocumentTemplate();
    }
}
