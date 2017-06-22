package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;

import org.apache.commons.beanutils.BeanUtils;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.OptionalTerms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by z418868 on 15/06/2017.
 */
public class ModelTenancyService {

    private final ModelTenancyFieldExtractor fieldExtractor;
    private final ModelTenancyDocumentTemplateLoader templateLoader;
    private final EmptySectionRemovingCallback emptySectionRemovingCallback;
    private final ModelTenancyJsonTemplateLoader modelTenancyJsonTemplateLoader;

    public ModelTenancyService(ModelTenancyDocumentTemplateLoader templateLoader,
                               ModelTenancyFieldExtractor fieldExtractor,
                               ModelTenancyJsonTemplateLoader modelTenancyJsonTemplateLoader) {
        this.templateLoader = templateLoader;
        this.fieldExtractor = fieldExtractor;
        emptySectionRemovingCallback = new EmptySectionRemovingCallback(deleteEmptySectionFields());
        this.modelTenancyJsonTemplateLoader = modelTenancyJsonTemplateLoader;
    }

    public ModelTenancy getModelTenancytemplate() throws ModelTenancyServiceException {
        try {
            return modelTenancyJsonTemplateLoader.loadJsonTemplate();
        } catch (TemplateLoaderException e) {
            throw new ModelTenancyServiceException("Failed to load model tenancy template", e);
        }
    }

    public byte [] save(ModelTenancy modelTenancy) throws ModelTenancyServiceException {
        Map<String, Object> fields = fieldExtractor.extractFields(modelTenancy);
        byte [] bytes = executeMailMerge(fields);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        savePdf(bytes, os);
        return os.toByteArray();
    }

    private byte [] executeMailMerge(Map<String, Object> fields) throws ModelTenancyServiceException {

        try {
            Document template = templateLoader.loadDocumentTemplate();
            template.getMailMerge().setTrimWhitespaces(true);
            template.getMailMerge().setFieldMergingCallback(emptySectionRemovingCallback);
            template.getMailMerge().execute(getFieldnames(fields), getValues(fields));
            template.updateFields();
            template.updatePageLayout();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            template.save(out, SaveFormat.DOCX);
            return out.toByteArray();
        } catch (Exception e) {
            throw new ModelTenancyServiceException("Failed to execute mail merge", e);
        }
    }

    private String [] getFieldnames(Map<String, Object> map) {
        return  map.entrySet().stream().map(Map.Entry::getKey).toArray(String[]::new);
    }

    private Object [] getValues(Map<String, Object> map) {
        return  map.entrySet().stream().map(Map.Entry::getValue).toArray();
    }

    private void savePdf(byte [] bytes, OutputStream outputStream) throws ModelTenancyServiceException {
        try {
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setUseHighQualityRendering(true);
            Document doc = new Document(new ByteArrayInputStream(bytes));
            doc.save(outputStream, saveOptions);
        } catch (Exception e) {
            throw new ModelTenancyServiceException("Failed to save pdf", e);
        }
    }

    private final Set<String> deleteEmptySectionFields() {
        Set<String> deleteWholeSectionFields = new HashSet<>();
        // add al of the optional sections.
        try {
            deleteWholeSectionFields.addAll(BeanUtils.describe(new OptionalTerms()).keySet());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to extract optional section fields", e);
        }

        // other fields with sections
        Collections.addAll(deleteWholeSectionFields,
                "hmoString",
                "rentPressureZoneString",
                "communicationsAgreementType");
        return deleteWholeSectionFields;
    }
}
