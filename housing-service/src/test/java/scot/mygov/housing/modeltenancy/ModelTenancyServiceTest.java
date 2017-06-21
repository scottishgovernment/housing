package scot.mygov.housing.modeltenancy;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.housing.HousingConfiguration;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

import javax.inject.Inject;

/**
 * Created by z418868 on 20/06/2017.
 */
public class ModelTenancyServiceTest {

    private final ObjectMother om = new ObjectMother();
    private final HousingConfiguration config = new HousingConfiguration();
    private final ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();

    @Test
    public void greenpath() throws ModelTenancyServiceException {

        TemplateLoader templateLoader = new TemplateLoader(config.getModelTenancyTemplatePath());
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor);
        ModelTenancy modelTenancy = om.anyTenancy();
        sut.save(modelTenancy);
    }

    @Test(expected = ModelTenancyServiceException.class)
    public void loaderThrowsException() throws ModelTenancyServiceException, TemplateLoaderException {

        TemplateLoader templateLoader = exceptionThrowingTemplateLoader();
        ModelTenancyService sut = new ModelTenancyService(templateLoader, fieldExtractor);
        ModelTenancy modelTenancy = om.anyTenancy();
        sut.save(modelTenancy);
    }

    private TemplateLoader exceptionThrowingTemplateLoader() throws TemplateLoaderException {
        TemplateLoader loader = Mockito.mock(TemplateLoader.class);
        Mockito.when(loader.loadTemplate()).thenThrow(new TemplateLoaderException("Arg", null));
        return loader;
    }
}
