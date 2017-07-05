package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.AsposeLicense;

import javax.inject.Inject;
import java.io.InputStream;

public class ModelTenancyDocumentTemplateLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ModelTenancyDocumentTemplateLoader.class);

    private static final String PATH = "/templates/model-tenancy-agreement.docx";

    private final Document template;

    @Inject
    public ModelTenancyDocumentTemplateLoader(AsposeLicense license) {
        this(PATH);
    }

    ModelTenancyDocumentTemplateLoader(String path) {
        this.template = loadDocument(path);
    }

    private static Document loadDocument(String path) {
        StopWatch watch = new StopWatch();
        watch.start();
        InputStream is = ModelTenancyDocumentTemplateLoader.class.getResourceAsStream(path);
        Document template;
        try {
            template = new Document(is);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load template", ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
        // remove any comments in the template
        template.getChildNodes(NodeType.COMMENT, true).clear();
        LOG.info("Loaded Word document in {}ms", watch.getTime());
        return template;
    }


    public Document loadDocumentTemplate() {
        return template.deepClone();
    }

}
