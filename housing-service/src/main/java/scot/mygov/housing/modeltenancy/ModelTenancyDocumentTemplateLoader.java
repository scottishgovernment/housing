package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
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

    public ModelTenancyDocumentTemplateLoader(String path) {
        try {
            long now = System.currentTimeMillis();
            InputStream in = this.getClass().getResourceAsStream(path);
            template = new Document(in);
            // remove any comments in the template
            NodeCollection comments = template.getChildNodes(NodeType.COMMENT, true);
            comments.clear();
            LOG.info("Loaded template.  took " + (System.currentTimeMillis() - now) + " millis");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load template", e);
        }
    }

    public Document loadDocumentTemplate() {
        return template.deepClone();
    }
}
