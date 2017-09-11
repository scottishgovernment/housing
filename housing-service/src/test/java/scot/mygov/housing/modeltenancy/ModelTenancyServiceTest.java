package scot.mygov.housing.modeltenancy;

import com.aspose.words.Document;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.housing.AsposeLicense;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTenancyServiceTest {

    private final ObjectMother om = new ObjectMother();

    private final HousingConfiguration config = new HousingConfiguration();

    private final ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();

    private final ModelTenancyJsonTemplateLoader jsonTemplateLoader = new ModelTenancyJsonTemplateLoader();

    @Test
    public void canGeneratePDF() throws Exception {

        // ARRANGE
        ModelTenancyDocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);

        // ACT
        ModelTenancy modelTenancy = om.anyTenancy();
        byte [] result = sut.save(modelTenancy, DocumentType.PDF);

        // ASSERT - can parse it as a pdf
        PDDocument document = PDDocument.load(new ByteArrayInputStream(result));
        document.close();
    }

    @Test
    public void canGenerateWordDocument() throws Exception {

        // ARRANGE
        ModelTenancyDocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);

        // ACT
        ModelTenancy modelTenancy = om.anyTenancy();
        byte [] result = sut.save(modelTenancy, DocumentType.WORD);

        // ASSERT - can parse it as a pdf
        Document document = new Document(new ByteArrayInputStream(result));
    }

    @Test(expected = ModelTenancyServiceException.class)
    public void loaderThrowsExceptionOnSave() throws ModelTenancyServiceException, TemplateLoaderException {

        ModelTenancyDocumentTemplateLoader templateLoader = exceptionThrowingTemplateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);
        ModelTenancy modelTenancy = om.anyTenancy();
        sut.save(modelTenancy, DocumentType.PDF);
    }

    @Test
    public void canGetModelTenancyTemplate() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyDocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);

        // ACT
        ModelTenancy modelTenancy = sut.getModelTenancyTemplate();

        // ASSERT
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getContentsAndConditions()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getLocalAuthorityTaxesAndCharges()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getAlterations()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getPrivateGarden()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getRoof()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getBinsAndRecycling()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getStorage()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getDangerousSubstances()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getPets()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getSmoking()));
        assertTrue(isNotEmpty(modelTenancy.getOptionalTerms().getLiquidPetroleumGas()));

    }

    @Test(expected=ModelTenancyServiceException.class)
    public void modelTenancyTemplateException() throws ModelTenancyServiceException, TemplateLoaderException {

        // ARRANGE
        ModelTenancyDocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, exceptionThrowingJsonTemplateLoader());

        // ACT
        ModelTenancy modelTenancy = sut.getModelTenancyTemplate();

        // ASSERT - see excepted exception

    }

    private ModelTenancyJsonTemplateLoader exceptionThrowingJsonTemplateLoader() throws TemplateLoaderException {
        ModelTenancyJsonTemplateLoader loader = mock(ModelTenancyJsonTemplateLoader.class);
        when(loader.loadJsonTemplate()).thenThrow(new TemplateLoaderException("Arg", null));
        return loader;
    }

    private ModelTenancyDocumentTemplateLoader templateLoader() {
        return new ModelTenancyDocumentTemplateLoader(new AsposeLicense(null));
    }

    private ModelTenancyDocumentTemplateLoader exceptionThrowingTemplateLoader() throws TemplateLoaderException {
        ModelTenancyDocumentTemplateLoader loader = mock(ModelTenancyDocumentTemplateLoader.class);
        when(loader.loadDocumentTemplate()).thenThrow(new TemplateLoaderException("Arg", null));
        return loader;
    }
}
