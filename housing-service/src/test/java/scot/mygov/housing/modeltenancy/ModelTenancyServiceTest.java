package scot.mygov.housing.modeltenancy;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.housing.AsposeLicense;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

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
