package scot.mygov.housing.forms.modeltenancy;

import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.documents.DocumentType;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import java.util.Map;

public class ModelTenancyService {

    public static final String DOCUMENT_TEMPLATE_PATH = "/templates/model-tenancy-agreement-with-notes.docx";

    private final ModelTenancyFieldExtractor fieldExtractor;

    private final DocumentGenerator documentGenerator;

    public ModelTenancyService(DocumentTemplateLoader templateLoader, ModelTenancyFieldExtractor fieldExtractor) {
        this.fieldExtractor = fieldExtractor;
        this.documentGenerator = new DocumentGenerator(templateLoader);
    }

    public byte[] save(ModelTenancy tenancy, DocumentType type) throws ModelTenancyServiceException {
        Map<String, Object> fields = fieldExtractor.extractFields(tenancy);
        try {
            return documentGenerator.save(fields, type, new ModelTenancyMergingCallback(tenancy));
        } catch (DocumentGeneratorException e) {
            throw new ModelTenancyServiceException("Failed to generate document", e);
        }
    }
}
