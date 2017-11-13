package scot.mygov.housing.forms;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentGeneratorException;
import scot.mygov.documents.DocumentType;
import scot.mygov.housing.MetricName;

import java.util.Map;

public class DocumentGenerationService <T> {

    private final DocumentGenerator documentGenerator;

    private final DocumentGeneratorServiceListener listener;

    private final FieldExtractor<T> fieldExtractor;

    private final IFieldMergingCallbackFactory<T> fieldMergingCallbackFactory;

    private final Timer responseTimes;

    private final Counter requestCounter;

    private final Counter errorCounter;

    private final Meter requestMeter;

    private final Meter errorMeter;

    public DocumentGenerationService(
            DocumentGenerator documentGenerator,
            DocumentGeneratorServiceListener listener,
            FieldExtractor<T> fieldExtractor,
            IFieldMergingCallbackFactory<T> fieldMergingCallbackFactory,
            MetricRegistry registry) {

        this.fieldExtractor = fieldExtractor;
        this.documentGenerator = documentGenerator;
        this.listener = listener;
        this.fieldMergingCallbackFactory = fieldMergingCallbackFactory;
        this.responseTimes = registry.timer(MetricName.RESPONSE_TIMES.name(this));
        this.requestCounter = registry.counter(MetricName.REQUESTS.name(this));
        this.errorCounter = registry.counter(MetricName.ERRORS.name(this));
        this.requestMeter = registry.meter(MetricName.REQUEST_RATE.name(this));
        this.errorMeter = registry.meter(MetricName.ERROR_RATE.name(this));
    }

    public byte[] save(T model, DocumentType type) throws DocumentGenerationServiceException {
        Timer.Context timer = responseTimes.time();
        requestCounter.inc();
        requestMeter.mark();

        Map<String, Object> fields = fieldExtractor.extractFields(model);
        listener.onGenerateStart(model);

        try {
            byte [] docBytes = documentGenerator.save(fields, type, fieldMergingCallbackFactory.newCallback(model));
            timer.stop();
            listener.onGenerateDone(model, docBytes);
            return docBytes;
        } catch (DocumentGeneratorException e) {
            listener.onGenerateException(model, e);
            errorCounter.inc();
            errorMeter.mark();
            throw new DocumentGenerationServiceException("Failed to generate document", e);
        }
    }
}