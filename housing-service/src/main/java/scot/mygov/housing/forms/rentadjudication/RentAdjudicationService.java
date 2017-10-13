package scot.mygov.housing.forms.rentadjudication;

import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;

import java.util.Collections;

public class RentAdjudicationService {

    public static final String DOCUMENT_TEMPLATE_PATH = "/templates/rent-adjudication.docx";

    RentAdjudicationFieldExtractor fieldExtractor;

    DocumentGenerator documentGenerator;

    public RentAdjudicationService(DocumentTemplateLoader templateLoader, RentAdjudicationFieldExtractor fieldExtractor) {
        this.documentGenerator = new DocumentGenerator(templateLoader, Collections.emptySet());
        this.fieldExtractor = fieldExtractor;
    }

    public byte[] save(RentAdjudication model, DocumentType type) throws RentAdjudicationServiceException {
        try {
            return documentGenerator.save(fieldExtractor.extractFields(model), type);
        } catch (DocumentGeneratorException e) {
            throw new RentAdjudicationServiceException("Failed to generate rent adjudication", e);
        }
    }
}
