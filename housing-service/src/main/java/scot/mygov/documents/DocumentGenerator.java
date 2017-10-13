package scot.mygov.documents;

import com.aspose.words.Document;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generates documents from a ampa of fields.
 */
public class DocumentGenerator {

    private final DocumentTemplateLoader templateLoader;
    private final EmptySectionRemovingCallback emptySectionRemovingCallback;

    public DocumentGenerator(DocumentTemplateLoader templateLoader, Set<String> emptySectionFields) {
        this.templateLoader = templateLoader;
        this.emptySectionRemovingCallback = new EmptySectionRemovingCallback(emptySectionFields);
    }

    public byte[] save(Map<String, Object> fields, DocumentType type) throws DocumentGeneratorException {
        byte[] mergedDocument = executeMailMerge(fields);

        if (type == DocumentType.WORD) {
            return mergedDocument;
        }

        // render as a PDF
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        savePdf(mergedDocument, os);
        return os.toByteArray();
    }

    private byte[] executeMailMerge(Map<String, Object> fields) throws DocumentGeneratorException {
        Document template = templateLoader.loadDocumentTemplate();
        template.getMailMerge().setTrimWhitespaces(true);
        template.getMailMerge().setFieldMergingCallback(emptySectionRemovingCallback);

        try {
            List<String> fieldnames = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            fields.entrySet().stream().forEach(entry -> {
                fieldnames.add(entry.getKey());
                values.add(entry.getValue());
            });
            template.getMailMerge().execute(
                    fieldnames.toArray(new String[fieldnames.size()]),
                    values.toArray());
            template.updateFields();
            template.updatePageLayout();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            template.save(out, SaveFormat.DOCX);
            return out.toByteArray();
        } catch (Exception e) {
            throw new DocumentGeneratorException("Failed to execute mail merge", e);
        }
    }

    private static void savePdf(byte[] wordDocument, OutputStream outputStream) throws DocumentGeneratorException {
        try {
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setUseHighQualityRendering(true);
            Document doc = new Document(new ByteArrayInputStream(wordDocument));
            doc.save(outputStream, saveOptions);
        } catch (Exception e) {
            throw new DocumentGeneratorException("Failed to convert word document to pdf", e);
        }
    }
}
