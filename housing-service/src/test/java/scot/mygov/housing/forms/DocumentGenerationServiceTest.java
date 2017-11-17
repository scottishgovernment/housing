package scot.mygov.housing.forms;

import com.codahale.metrics.MetricRegistry;
import org.junit.Test;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.AsposeLicense;
import scot.mygov.housing.forms.modeltenancy.ModelTenancyFieldExtractor;
import scot.mygov.housing.forms.modeltenancy.ModelTenancyMergingCallback;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyObjectMother;

public class DocumentGenerationServiceTest {

    ModelTenancyObjectMother om = new ModelTenancyObjectMother();

    @Test
    public void canSaveWithNoMergingCallback()  throws Exception{

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/model-tenancy-agreement-with-notes.docx", null);
        DocumentGenerationService sut
                = new DocumentGenerationService<ModelTenancy>(
                new DocumentGenerator(templateLoader), new ModelTenancyFieldExtractor(), new MetricRegistry());

        sut.save(om.anyTenancy(), DocumentType.PDF);
    }

    @Test
    public void canSaveWithMergingCallback()  throws Exception{

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/model-tenancy-agreement-with-notes.docx", null);
        DocumentGenerationService sut
                = new DocumentGenerationService<ModelTenancy>(
                new DocumentGenerator(templateLoader), new ModelTenancyFieldExtractor(),
                form -> new ModelTenancyMergingCallback(om.anyTenancy()),
                new MetricRegistry());

        sut.save(om.anyTenancy(), DocumentType.PDF);
    }
}
