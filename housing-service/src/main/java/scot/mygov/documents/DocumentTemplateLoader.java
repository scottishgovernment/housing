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
 * Loads a document template from the classpath.  Load the template and then clones it on each use.
 */
public interface DocumentTemplateLoader {

    Document loadDocumentTemplate();

}
