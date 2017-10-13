package scot.mygov.documents;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Types of document supported by the DocumentGenerator.
 */
public enum DocumentType {
    PDF("application/pdf", "pdf"), WORD("application/docx", "docx");

    private static final Logger LOG = LoggerFactory.getLogger(DocumentType.class);

    private final String contentType;
    private final String extension;

    DocumentType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }

    public static DocumentType determineDocumentType(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return DocumentType.PDF;
        }
        try {
            return DocumentType.valueOf(contentType);
        } catch (IllegalArgumentException e) {
            LOG.warn("Unrecognised document type: " + contentType, e);
            // default to PDF if the param is invalid
            return DocumentType.PDF;
        }
    }
}
