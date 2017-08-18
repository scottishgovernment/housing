package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;
import org.apache.commons.beanutils.BeanUtils;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.OptionalTerms;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.addAll;

public class ModelTenancyService {

    private final ModelTenancyFieldExtractor fieldExtractor;
    private final ModelTenancyDocumentTemplateLoader templateLoader;
    private final EmptySectionRemovingCallback emptySectionRemovingCallback;
    private final ModelTenancyJsonTemplateLoader modelTenancyJsonTemplateLoader;

    @Inject
    public ModelTenancyService(ModelTenancyDocumentTemplateLoader templateLoader,
                               ModelTenancyFieldExtractor fieldExtractor,
                               ModelTenancyJsonTemplateLoader modelTenancyJsonTemplateLoader) {
        this.templateLoader = templateLoader;
        this.fieldExtractor = fieldExtractor;
        emptySectionRemovingCallback = new EmptySectionRemovingCallback(fieldsToDeleteIfEmpty());
        this.modelTenancyJsonTemplateLoader = modelTenancyJsonTemplateLoader;
    }

    public ModelTenancy getModelTenancyTemplate() throws ModelTenancyServiceException {
        try {
            return modelTenancyJsonTemplateLoader.loadJsonTemplate();
        } catch (TemplateLoaderException e) {
            throw new ModelTenancyServiceException("Failed to load model tenancy template", e);
        }
    }

    public byte[] save(ModelTenancy modelTenancy) throws ModelTenancyServiceException {
        Map<String, Object> fields = fieldExtractor.extractFields(modelTenancy);
        byte[] mergedDocument = executeMailMerge(fields);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        savePdf(mergedDocument, os);
        return os.toByteArray();
    }

    private byte[] executeMailMerge(Map<String, Object> fields) throws ModelTenancyServiceException {
        try {
            Document template = templateLoader.loadDocumentTemplate();
            template.getMailMerge().setTrimWhitespaces(true);
            template.getMailMerge().setFieldMergingCallback(emptySectionRemovingCallback);

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
            throw new ModelTenancyServiceException("Failed to execute mail merge", e);
        }
    }

    private static void savePdf(byte[] wordDocument, OutputStream outputStream) throws ModelTenancyServiceException {
        try {
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setUseHighQualityRendering(true);
            Document doc = new Document(new ByteArrayInputStream(wordDocument));
            doc.save(outputStream, saveOptions);
        } catch (Exception e) {
            throw new ModelTenancyServiceException("Failed to convert word document to pdf", e);
        }
    }

    private static final Set<String> fieldsToDeleteIfEmpty() {
        Set<String> fields = new HashSet<>();
        // add al of the optional sections.
        try {
            fields.addAll(BeanUtils.describe(new OptionalTerms()).keySet());
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract optional section fields", e);
        }

        // other fields with sections
        addAll(fields, "hmoString", "rentPressureZoneString", "communicationsAgreementType");
        return fields;
    }
}
