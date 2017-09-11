package scot.mygov.housing.modeltenancy;

public enum DocumentType {
    PDF("application/pdf", "pdf"), WORD("application/docx", "docx");

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
}
