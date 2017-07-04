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

    private final ModelTenancy modelTenancyTemplate;

    public ModelTenancyJsonTemplateLoader() {
        modelTenancyTemplate = new ModelTenancy();
        loadOptionalTerms(modelTenancyTemplate);
    }

    public ModelTenancy loadJsonTemplate() {
        return modelTenancyTemplate;
    }

    private void loadOptionalTerms(ModelTenancy modeltenancy) {

        OptionalTerms terms = modeltenancy.getOptionalTerms();
        try {
            Map<String, String> values = BeanUtils.describe(modeltenancy.getOptionalTerms());
            for (String key : values.keySet()) {
                if ("class".equals(key)) {
                    continue;
                }
                String value = loadResource(key);
                // remove any single \n's but keep doubles.
                value = value.replaceAll("([^\n])\n(?!\n)", "$1 ");
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
