package scot.mygov.housing.modeltenancy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.OptionalTerms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by z418868 on 22/06/2017.
 */
public class ModelTenancyJsonTemplateLoader {

    private ModelTenancy modelTenancyTemplate = null;

    public ModelTenancy loadJsonTemplate() throws TemplateLoaderException {

        if (modelTenancyTemplate == null) {
            modelTenancyTemplate = new ModelTenancy();
            loadOptionalTerms(modelTenancyTemplate);
        }
        return modelTenancyTemplate;
    }

    private void loadOptionalTerms(ModelTenancy modeltenancy) throws TemplateLoaderException {

        OptionalTerms terms = modeltenancy.getOptionalTerms();
        try {
            Map<String, String> values = BeanUtils.describe(modeltenancy.getOptionalTerms());
            for (String key : values.keySet()) {
                if ("class".equals(key)) {
                    continue;
                }
                String value = loadResource(key);

                // hack attack, think of a better way if
                value = value.replace("\n\n", "[NEWLINKE]");
                value = value.replace("\n", "");
                value = value.replace("[NEWLINKE]", "\n");
                BeanUtils.setProperty(terms, key, value);
            }
        } catch (Exception e) {
            throw new TemplateLoaderException("Failed to load Json tempate", e);
        }
    }

    private String loadResource(String key) throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/optionalTerms/" + key + ".txt");
        return IOUtils.toString(in, "UTF-8");
    }
}
