package scot.mygov.housing.modeltenancy;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.housing.AsposeLicense;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

import static org.junit.Assert.assertTrue;

public class ModelTenancyServiceTest {

    private final ObjectMother om = new ObjectMother();

    private final HousingConfiguration config = new HousingConfiguration();

    private final ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();

    private final ModelTenancyJsonTemplateLoader jsonTemplateLoader = new ModelTenancyJsonTemplateLoader();

    @Test
    public void greenpathSave() throws ModelTenancyServiceException {

        ModelTenancyDocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);
        ModelTenancy modelTenancy = om.anyTenancy();
        sut.save(modelTenancy);
    }

    @Test(expected = ModelTenancyServiceException.class)
    public void loaderThrowsExceptionOnSave() throws ModelTenancyServiceException, TemplateLoaderException {

        ModelTenancyDocumentTemplateLoader templateLoader = exceptionThrowingTemplateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);
        ModelTenancy modelTenancy = om.anyTenancy();
        sut.save(modelTenancy);
    }

    @Test
    public void canGetModelTenancyTemplate() throws ModelTenancyServiceException {

        // ARRANGE
        ModelTenancyDocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);

        // ACT
        ModelTenancy modelTenancy = sut.getModelTenancytemplate();

        // ASSERT
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getContentsAndConditions()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getLocalAuthorityTaxesAndCharges()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getAlterations()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getPrivateGarden()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getRoof()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getBinsAndRecycling()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getStorage()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getDangerousSubstances()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getPets()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getSmoking()));
        assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getLiquidPetroleumGas()));

    }

    @Test(expected=ModelTenancyServiceException.class)
    public void modelTenancyTemplateException() throws ModelTenancyServiceException, TemplateLoaderException {

        // ARRANGE
        ModelTenancyDocumentTemplateLoader templateLoader = templateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, exceptionThrowingJsonTemplateLoader());

        // ACT
        ModelTenancy modelTenancy = sut.getModelTenancytemplate();

        // ASSERT - see excepted exception

    }

    private ModelTenancyJsonTemplateLoader exceptionThrowingJsonTemplateLoader() throws TemplateLoaderException {
        ModelTenancyJsonTemplateLoader loader = Mockito.mock(ModelTenancyJsonTemplateLoader.class);
        Mockito.when(loader.loadJsonTemplate()).thenThrow(new TemplateLoaderException("Arg", null));
        return loader;
    }

    private ModelTenancyDocumentTemplateLoader templateLoader() {
        return new ModelTenancyDocumentTemplateLoader(new AsposeLicense(null));
    }

    private ModelTenancyDocumentTemplateLoader exceptionThrowingTemplateLoader() throws TemplateLoaderException {
        ModelTenancyDocumentTemplateLoader loader = Mockito.mock(ModelTenancyDocumentTemplateLoader.class);
        Mockito.when(loader.loadDocumentTemplate()).thenThrow(new TemplateLoaderException("Arg", null));
        return loader;
    }
}
