package scot.mygov.documents;

import com.aspose.words.Document;

/**
 * Loads a document template from the classpath.  Load the template and then clones it on each use.
 */
public interface DocumentTemplateLoader {

    Document loadDocumentTemplate();

}
