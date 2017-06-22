package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by z418868 on 20/06/2017.
 */
public class ModelTenancyDocumentTemplateLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ModelTenancyDocumentTemplateLoader.class);

    private final String path;
    private Document template;

    public ModelTenancyDocumentTemplateLoader(String path) {
        this.path = path;
    }

    public Document loadDocumentTemplate() throws TemplateLoaderException {

        // if we have already loaded the tmeplate then return a clone of it.
        if (template != null) {
            return template.deepClone();
        }

        // load the template
        try {
            long now = System.currentTimeMillis();
            InputStream in = this.getClass().getResourceAsStream(path);
            template = new Document(in);

            // renove any comments in the template
            NodeCollection comments = template.getChildNodes(NodeType.COMMENT, true);
            comments.clear();

            LOG.info("Loaded template.  took " + (System.currentTimeMillis() - now) + " millis");

            return template.deepClone();
        } catch (Exception e) {
            throw new TemplateLoaderException("Failed to load template", e);
        }
    }
}
