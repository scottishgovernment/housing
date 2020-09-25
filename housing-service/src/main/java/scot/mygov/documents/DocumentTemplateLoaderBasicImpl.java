package scot.mygov.documents;

import com.aspose.words.Document;
import com.aspose.words.NodeType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.UnavailableResourceException;
import scot.mygov.housing.AsposeLicense;

import java.io.InputStream;

/**
 * Created by z418868 on 24/09/2020.
 */
public class DocumentTemplateLoaderBasicImpl implements DocumentTemplateLoader{

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTemplateLoader.class);

    private final Document template;

    public DocumentTemplateLoaderBasicImpl(String path, AsposeLicense license) {
        this.template = loadDocument(path);
    }

    private static Document loadDocument(String path) {
        LOG.info("loading {}", path);
        StopWatch watch = new StopWatch();
        watch.start();
        InputStream is = DocumentTemplateLoaderBasicImpl.class.getResourceAsStream(path);
        Document template;
        try {
            template = new Document(is);
        } catch (Exception ex) {
            throw new UnavailableResourceException("Failed to load template", ex);
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
