package scot.mygov.housing.forms.modeltenancy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import scot.mygov.UnavailableResourceException;
import scot.mygov.housing.forms.modeltenancy.model.MustIncludeTerms;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Supplier;

public class TermsUtil {

    private static final String CLASS = "class";

    // must match HousingConfiguration's legislationChangeDate2026 default
    private static final LocalDate LEGISLATION_CHANGE_DATE_2026 = LocalDate.of(2026, 10, 6);

    private static final String DATED_SUFFIX_2026 = "-2026";

    // overridable in tests, mirrors DateSwitchingDocumentTemplateLoader
    static Supplier<LocalDate> clock = LocalDate::now;

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
        if (!clock.get().isBefore(LEGISLATION_CHANGE_DATE_2026)) {
            InputStream dated = openResource(pathIn, key + DATED_SUFFIX_2026);
            if (dated != null) {
                return IOUtils.toString(dated, StandardCharsets.UTF_8);
            }
        }
        InputStream in = openResource(pathIn, key);
        return IOUtils.toString(in, StandardCharsets.UTF_8);
    }

    private static InputStream openResource(String pathIn, String key) {
        Path path = Paths.get(pathIn, key + ".txt");
        return TermsUtil.class.getResourceAsStream(path.toString());
    }
}
