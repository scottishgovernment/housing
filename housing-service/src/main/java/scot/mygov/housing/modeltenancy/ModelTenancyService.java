package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;

import com.aspose.words.Section;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.InputStream;
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

    public ModelTenancyService(ModelTenancyFieldExtractor fieldExtractor) {
        this.fieldExtractor = fieldExtractor;
    }

    public byte [] save(ModelTenancy modelTenancy) throws ModelTenancyServiceException {
        Map<String, Object> fields = fieldExtractor.extractFields(modelTenancy);
        Document template = loadTemplate();
        byte [] mergedTemplateBytes = executeMailMerge(template, fields);
        byte [] velocityOutputBytes = executeVelocityTemplate(mergedTemplateBytes, fields);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        savePdf(velocityOutputBytes, os);
        return os.toByteArray();
    }

    private Document loadTemplate() throws ModelTenancyServiceException {
        try {
            InputStream in = this.getClass().getResourceAsStream("/templates/model-tenancy-agreement.docx");
            Document doc = new Document(in);
            doc.getMailMerge().setTrimWhitespaces(true);
            NodeCollection comments = doc.getChildNodes(NodeType.COMMENT, true);
            comments.clear();
            return doc;
        } catch (Exception e) {
            throw new ModelTenancyServiceException("Failed to load template", e);
        }
    }

    private byte [] executeMailMerge(Document template, Map<String, Object> fields) throws ModelTenancyServiceException {
        try {
            Set<String> deleteWholeSectionFields = new HashSet<>();
            Collections.addAll(deleteWholeSectionFields,
                    "hmoString",
                    "rentPressureZoneString",
                    "communicationsAgreementType");
            template.getMailMerge().setFieldMergingCallback(new IFieldMergingCallback() {
                @Override
                public void fieldMerging(FieldMergingArgs fieldMergingArgs) throws Exception {

                    if (!deleteWholeSectionFields.contains(fieldMergingArgs.getFieldName())) {
                        return;
                    }
                    boolean isEmptyOrNull = fieldMergingArgs.getFieldValue() == null || fieldMergingArgs.getFieldValue().toString().length() == 0;

                    if (!isEmptyOrNull) {
                        return;
                    }
                    fieldMergingArgs.getField().getStart().getAncestor(Section.class).remove();
                }

                @Override
                public void imageFieldMerging(ImageFieldMergingArgs imageFieldMergingArgs) throws Exception {
                    // no action needed
                }
            });
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

    private byte [] executeVelocityTemplate(byte [] templateBytes, Map<String, Object> fields) throws ModelTenancyServiceException {
       try {
           ByteArrayInputStream in = new ByteArrayInputStream(templateBytes);
           IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
           IContext context = report.createContext(fields);
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           report.process(context, out);
           return out.toByteArray();
       } catch (Exception e) {
           throw new ModelTenancyServiceException("Failed to execute velocity phase", e);
       }
    }

    private void savePdf(byte [] velocityOutputBytes, OutputStream outputStream) throws ModelTenancyServiceException {
        try {
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setUseHighQualityRendering(true);
            Document doc = new Document(new ByteArrayInputStream(velocityOutputBytes));
            doc.save(outputStream, saveOptions);
        } catch (Exception e) {
            throw new ModelTenancyServiceException("Failed to save pdf", e);
        }
    }
}
