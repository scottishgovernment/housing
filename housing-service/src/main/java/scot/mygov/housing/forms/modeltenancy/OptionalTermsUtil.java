package scot.mygov.housing.forms.modeltenancy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import scot.mygov.UnavailableResourceException;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class OptionalTermsUtil {

    private OptionalTermsUtil() {
        // utility class
    }

    public static OptionalTerms defaultTerms() {
        OptionalTerms terms = new OptionalTerms();
        try {
            Map<String, String> values = BeanUtils.describe(terms);
            for (String key : values.keySet()) {
                if ("class".equals(key)) {
                    continue;
                }
                String value = loadResource("/optionalTerms/", key);
                // remove any single \n's but keep doubles.
                value = value.replaceAll("([^\n])\n(?!\n)", "$1 ");
                BeanUtils.setProperty(terms, key, value);
            }
            return terms;
        } catch (Exception e) {
            throw new UnavailableResourceException("Failed to load Json tempate", e);
        }
    }

    public static OptionalTerms defaultEasyreadNotes() {
        OptionalTerms terms = new OptionalTerms();
        try {
            Map<String, String> values = BeanUtils.describe(terms);
            for (String key : values.keySet()) {
                if ("class".equals(key)) {
                    continue;
                }
                String value = loadResource("/optionalTerms/easyread/", key);
                // remove any single \n's but keep doubles.
                value = value.replaceAll("([^\n])\n(?!\n)", "$1 ");
                BeanUtils.setProperty(terms, key, value);
            }
            return terms;
        } catch (Exception e) {
            throw new UnavailableResourceException("Failed to load Json tempate", e);
        }
    }

    private static String loadResource(String pathIn, String key) throws IOException {
        Path path = Paths.get(pathIn, key + ".txt");
        InputStream in = OptionalTermsUtil.class.getResourceAsStream(path.toString());
        return IOUtils.toString(in, "UTF-8");
    }
}
