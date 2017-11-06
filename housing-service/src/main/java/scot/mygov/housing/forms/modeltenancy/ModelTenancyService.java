package scot.mygov.housing.forms.modeltenancy;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.MetricName;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import java.util.Map;

public class ModelTenancyService {

    public static final String DOCUMENT_TEMPLATE_PATH = "/templates/model-tenancy-agreement-with-notes.docx";

    private final ModelTenancyFieldExtractor fieldExtractor;

    private final DocumentGenerator documentGenerator;

    private final Timer responseTimes;

    private final Counter requestCounter;

    private final Counter unchangedUtilitiesCounter;

    private final Counter changedUtilitiesCounter;

    private final Counter errorCounter;

    private final Meter requestMeter;

    private final Meter errorMeter;

    public ModelTenancyService(
            DocumentGenerator documentGenerator,
            ModelTenancyFieldExtractor fieldExtractor,
            MetricRegistry registry) {

        this.fieldExtractor = fieldExtractor;
        this.documentGenerator = documentGenerator;
        this.responseTimes = registry.timer(MetricName.RESPONSE_TIMES.name(this));
        this.requestCounter = registry.counter(MetricName.REQUESTS.name(this));
        this.errorCounter = registry.counter(MetricName.ERRORS.name(this));
        this.requestMeter = registry.meter(MetricName.REQUEST_RATE.name(this));
        this.errorMeter = registry.meter(MetricName.ERROR_RATE.name(this));
        this.unchangedUtilitiesCounter = registry.counter(MetricRegistry.name(this.getClass(), "unchanged-utilities"));
        this.changedUtilitiesCounter = registry.counter(MetricRegistry.name(this.getClass(), "changed-utilities"));
    }

    public byte[] save(ModelTenancy tenancy, DocumentType type) throws ModelTenancyServiceException {
        Timer.Context timer = responseTimes.time();
        requestCounter.inc();
        requestMeter.mark();
        updateUtilitiesCounter(tenancy);

        Map<String, Object> fields = fieldExtractor.extractFields(tenancy);
        try {
            byte [] docBytes = documentGenerator.save(fields, type, new ModelTenancyMergingCallback(tenancy));
            timer.stop();
            return docBytes;
        } catch (DocumentGeneratorException e) {
            errorCounter.inc();
            errorMeter.mark();
            throw new ModelTenancyServiceException("Failed to generate document", e);
        }
    }

    private void updateUtilitiesCounter(ModelTenancy tenancy) {
        // If the user did not change the utilities field then count this.
        //
        // We are counting this so that we can measure if people are correctly changing the list of utilities in
        // square brackets or not.
        if (tenancy.getOptionalTerms().getUtilities().equals(OptionalTermsUtil.defaultTerms().getUtilities())) {
            unchangedUtilitiesCounter.inc();
        } else {
            changedUtilitiesCounter.inc();
        }
    }
}
