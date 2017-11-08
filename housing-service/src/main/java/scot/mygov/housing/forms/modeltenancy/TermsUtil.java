package scot.mygov.housing.forms.modeltenancy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import scot.mygov.UnavailableResourceException;
import scot.mygov.housing.forms.modeltenancy.model.MustIncludeTerms;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TermsUtil {

    private static final String CLASS = "class";

    private TermsUtil() {
        // utility class
    }

    public static OptionalTerms defaultOptionalTerms() {
        OptionalTerms terms = new OptionalTerms();
        loadTerms(terms, "/optionalTerms/");
        return terms;
    }

    public static MustIncludeTerms defaultMustIncludeTerms() {
        MustIncludeTerms terms = new MustIncludeTerms();
        loadTerms(terms, "/mustIncludeTerms/");
        return terms;
    }

    public static OptionalTerms defaultEasyreadNotes() {
        OptionalTerms terms = new OptionalTerms();
        loadTerms(terms, "/optionalTerms/easyread/");
        return terms;
    }

    private static Object loadTerms(Object terms, String path) {
        try {
            Map<String, String> values = BeanUtils.describe(terms);
            for (String key : values.keySet()) {
                if (CLASS.equals(key)) {
                    continue;
                }
                String value = loadResource(path, key);
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
        InputStream in = TermsUtil.class.getResourceAsStream(path.toString());
        return IOUtils.toString(in, "UTF-8");
    }
}
