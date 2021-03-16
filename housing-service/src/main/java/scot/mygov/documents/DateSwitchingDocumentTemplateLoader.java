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

    NavigableMap<LocalDate, DocumentTemplateLoader> loaders = new TreeMap<>();

    Supplier<LocalDate> localDateSupplier = () -> LocalDate.now();

    public void addDocument(LocalDate fromDate, DocumentTemplateLoader loader) {
        loaders.put(fromDate, loader);
    }

    @Override
    public Document loadDocumentTemplate() {
        LocalDate today = localDateSupplier.get();
        LOG.info("loadDocumentTemplate, date is {}", today);
        Map.Entry<LocalDate, DocumentTemplateLoader> entry = loaders.floorEntry(today);
        if (entry == null) {
            return null;
        }
        LocalDate dateUsed = entry.getKey();
        DocumentTemplateLoader loader = entry.getValue();
        LOG.info("Using version for date {}", dateUsed);
        return loader.loadDocumentTemplate();
    }

}
