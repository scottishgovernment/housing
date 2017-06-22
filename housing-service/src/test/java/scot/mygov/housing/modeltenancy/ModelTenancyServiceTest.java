package scot.mygov.housing.modeltenancy;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

/**
 * Created by z418868 on 20/06/2017.
 */
public class ModelTenancyServiceTest {

    private final ObjectMother om = new ObjectMother();
    private final HousingConfiguration config = new HousingConfiguration();
    private final ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();
    private final ModelTenancyJsonTemplateLoader jsonTemplateLoader = new ModelTenancyJsonTemplateLoader();

    @Test
    public void greenpathSave() throws ModelTenancyServiceException {

        ModelTenancyDocumentTemplateLoader templateLoader = new ModelTenancyDocumentTemplateLoader(config.getModelTenancyTemplatePath());
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
        ModelTenancyDocumentTemplateLoader templateLoader = new ModelTenancyDocumentTemplateLoader(config.getModelTenancyTemplatePath());
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor, jsonTemplateLoader);

        // ACT
        ModelTenancy modelTenancy = sut.getModelTenancytemplate();

        // ASSERT
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getContentsAndConditions()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getLocalAuthorityTaxesAndCharges()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getAlterations()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getPrivateGarden()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getRoof()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getBinsAndRecycling()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getStorage()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getDangerousSubstances()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getPets()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getSmoking()));
        Assert.assertTrue(StringUtils.isNotEmpty(modelTenancy.getOptionalTerms().getLiquidPetroleumGas()));

    }

    @Test(expected=ModelTenancyServiceException.class)
    public void modelTenancyTemplateException() throws ModelTenancyServiceException, TemplateLoaderException {

        // ARRANGE
        ModelTenancyDocumentTemplateLoader templateLoader = new ModelTenancyDocumentTemplateLoader(config.getModelTenancyTemplatePath());
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

    private ModelTenancyDocumentTemplateLoader exceptionThrowingTemplateLoader() throws TemplateLoaderException {
        ModelTenancyDocumentTemplateLoader loader = Mockito.mock(ModelTenancyDocumentTemplateLoader.class);
        Mockito.when(loader.loadDocumentTemplate()).thenThrow(new TemplateLoaderException("Arg", null));
        return loader;
    }
}
