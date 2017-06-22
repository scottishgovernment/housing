package scot.mygov.housing.modeltenancy;

import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

/**
 * Created by z418868 on 22/06/2017.
 */
public class ModelTenancyJsonTemplateLoaderTest {

    private final ObjectMother om = new ObjectMother();

    @Test
    public void canLoadJsontemplate() throws TemplateLoaderException {
        ModelTenancyJsonTemplateLoader loader = new ModelTenancyJsonTemplateLoader();

        ModelTenancy tenancy = loader.loadJsonTemplate();
    }


}
