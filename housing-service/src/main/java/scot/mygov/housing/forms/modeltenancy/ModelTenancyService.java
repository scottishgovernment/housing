package scot.mygov.housing.forms.modeltenancy;

import org.apache.commons.beanutils.BeanUtils;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.documents.DocumentType;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.housing.forms.InitialisationFailedException;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.addAll;

public class ModelTenancyService {

    public static final String DOCUMENT_TEMPLATE_PATH = "/templates/model-tenancy-agreement-with-notes.docx";

    private final ModelTenancyFieldExtractor fieldExtractor;

    private final DocumentGenerator documentGenerator;

    public ModelTenancyService(DocumentTemplateLoader templateLoader, ModelTenancyFieldExtractor fieldExtractor) {
        this.fieldExtractor = fieldExtractor;
        this.documentGenerator = new DocumentGenerator(templateLoader, fieldsToDeleteIfEmpty());
    }

    public byte[] save(ModelTenancy modelTenancy, DocumentType type) throws ModelTenancyServiceException {
        Map<String, Object> fields = fieldExtractor.extractFields(modelTenancy);
        try {
            return documentGenerator.save(fields, type);
        } catch (DocumentGeneratorException e) {
            throw new ModelTenancyServiceException("Failed to generate document", e);
        }
    }

    private static final Set<String> fieldsToDeleteIfEmpty() {
        Set<String> fields = new HashSet<>();
        // add al of the optional sections.
        try {
            fields.addAll(BeanUtils.describe(new OptionalTerms()).keySet());
        } catch (Exception e) {
            throw new InitialisationFailedException("Failed to extract optional section fields", e);
        }

        // other fields with sections
        addAll(fields, "hmoString", "rentPressureZoneString", "communicationsAgreementType");
        return fields;
    }
}
