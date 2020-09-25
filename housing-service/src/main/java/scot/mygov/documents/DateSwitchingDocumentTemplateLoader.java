package scot.mygov.documents;

import com.aspose.words.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

/**
 * DocumentTemplateLoader that can load a different template based on the date being called.
 *
 * The document loaded will be the most recent one whose threshold is either today or later.
 */
public class DateSwitchingDocumentTemplateLoader implements DocumentTemplateLoader {

    private static final Logger LOG = LoggerFactory.getLogger(DateSwitchingDocumentTemplateLoader.class);

    SortedMap<LocalDate, DocumentTemplateLoader> loaders = new TreeMap<>();

     Supplier<LocalDate> localDateSuplier = () -> LocalDate.now();

    public void addDocument(LocalDate fromDate, DocumentTemplateLoader loader) {
        loaders.put(fromDate, loader);
    }

    @Override
    public Document loadDocumentTemplate() {
        DocumentTemplateLoader loader = null;
        LocalDate dateUsed = null;
        LOG.info("loadDocumentTemplate, date is {}", localDateSuplier.get());
        for (Map.Entry<LocalDate, DocumentTemplateLoader> entry : loaders.entrySet()) {
            if (todayOrAfter(entry.getKey())) {
                dateUsed = entry.getKey();
                loader = entry.getValue();
            }
        }
        LOG.info("Using version for date {}", dateUsed);
        return loader == null ? null : loader.loadDocumentTemplate();
    }

    boolean todayOrAfter(LocalDate date) {
        LocalDate today = localDateSuplier.get();
        return date.equals(today) || today.isAfter(date);
    }

}
