package scot.mygov.housing;

import com.aspose.words.IFieldMergingCallback;
import com.codahale.metrics.MetricRegistry;
import dagger.Module;
import dagger.Provides;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.config.Configuration;
import scot.mygov.documents.DocumentGenerator;
import scot.mygov.documents.DocumentTemplateLoader;
import scot.mygov.housing.cpi.CPIService;

import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.DocumentGeneratorServiceListener;
import scot.mygov.housing.forms.DocumentGeneratorServiceListenerAdaptor;
import scot.mygov.housing.forms.FieldExtractor;
import scot.mygov.housing.forms.modeltenancy.ModelTenancyFieldExtractor;
import scot.mygov.housing.forms.modeltenancy.ModelTenancyMergingCallback;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.nonprovisionofdocumentation.NonProvisionOfDocumentationFieldExtractor;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;
import scot.mygov.housing.forms.rentadjudication.RentAdjudicationFieldExtractor;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyValidatorFactory;
import scot.mygov.housing.postcode.PostcodeService;
import scot.mygov.housing.postcode.MapcloudPostcodeService;
import scot.mygov.housing.rpz.ElasticSearchRPZService;
import scot.mygov.housing.rpz.RPZService;
import scot.mygov.validation.Validator;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;

@Module(injects = Housing.class)
public class HousingModule {

    private static final Logger LOG = LoggerFactory.getLogger(HousingConfiguration.class);

    public static final String MAPCLOUD_TARGET = "mapcloudTarget";
    public static final String ELASTICSEARCH_TARGET = "esTarget";
    public static final String ES_RPZ_HEALTH_TARGET = "esRPZHealthTarget";

    private static final String APP_NAME = "housing";

    @Provides
    @Singleton
    HousingConfiguration configuration() {
        Configuration<HousingConfiguration> configuration = Configuration
                .load(new HousingConfiguration(), APP_NAME)
                .validate();
        LOG.info("{}", configuration);
        return configuration.getConfiguration();
    }

    @Provides
    @Named(MAPCLOUD_TARGET)
    WebTarget mapcloudTarget(Client client, HousingConfiguration configuration) {
        return client.target(configuration.getMapcloudURI());
    }

    @Provides
    @Named(ELASTICSEARCH_TARGET)
    WebTarget esTarget(Client client, HousingConfiguration configuration) {
        return client.target(configuration.getRpzDataURI());
    }

    @Provides
    @Named(ES_RPZ_HEALTH_TARGET)
    WebTarget rpzHealthTarget(Client client, HousingConfiguration configuration) {
        return client.target(configuration.getRpzHealthURI());
    }

    @Provides
    @Singleton
    Client client() {
        return new ResteasyClientBuilder().connectionPoolSize(10).build();
    }


    @Provides
    RPZService rpzService(Mapcloud mapcloud, @Named(ELASTICSEARCH_TARGET) WebTarget esTarget) {
        return new ElasticSearchRPZService(mapcloud, esTarget);
    }

    @Provides
    @Singleton
    AsposeLicense asposeLicense(HousingConfiguration configuration) {
        return new AsposeLicense(configuration.getAspose().getLicense());
    }

    @Provides
    @Singleton
    CPIService cpiService(HousingConfiguration configuration) {
        try {
            return new CPIService(configuration.getCpiDataURI().toURL());
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Failed to load CPI data from url"+configuration.getCpiDataURI(), e);
        }
    }

    @Provides
    @Singleton
    Mapcloud mapcloud(
            HousingConfiguration config,
            MetricRegistry registry,
            @Named(MAPCLOUD_TARGET) WebTarget mapcloudTarget) {
        return new Mapcloud(mapcloudTarget,
                config.getMapcloudUser(),
                config.getMapcloudPassword(),
                registry);
    }

    @Provides
    PostcodeService postcodeService(Mapcloud mapcloud) {
        return new MapcloudPostcodeService(mapcloud);
    }

    @Provides
    @Singleton
    MetricRegistry metricsRegistry() {
        return new MetricRegistry();
    }

    @Provides
    Validator<ModelTenancy> modelTenancyValidator() {
        return new ModelTenancyValidatorFactory().validator(false);
    }


    @Provides
    DocumentGenerationService<RentAdjudication> rentAdjudicationDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        String templatePath = "/templates/rent-adjudication.docx";
        DocumentTemplateLoader templateLoader = new DocumentTemplateLoader(templatePath, asposeLicense);
        FieldExtractor<RentAdjudication> fieldExtractor = new RentAdjudicationFieldExtractor();
        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
        DocumentGeneratorServiceListener<RentAdjudication> listener = new DocumentGeneratorServiceListenerAdaptor<>();
        IFieldMergingCallback fieldMergingCallback = null;

        return  new DocumentGenerationService<>(
                documentGenerator,
                listener,
                fieldExtractor,
                fieldMergingCallback,
                metricRegistry);
    }

    @Provides
    DocumentGenerationService<NonProvisionOfDocumentation> nonProvisionOfDocumentationDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        String templatePath = "/templates/non-provision-of-documentation.docx";
        DocumentTemplateLoader templateLoader = new DocumentTemplateLoader(templatePath, asposeLicense);
        FieldExtractor<NonProvisionOfDocumentation> fieldExtractor = new NonProvisionOfDocumentationFieldExtractor();
        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
        DocumentGeneratorServiceListener<RentAdjudication> listener = new DocumentGeneratorServiceListenerAdaptor<>();
        IFieldMergingCallback fieldMergingCallback = null;
        return  new DocumentGenerationService<>(
                documentGenerator,
                listener,
                fieldExtractor,
                fieldMergingCallback,
                metricRegistry);
    }

//
//    @Provides
//    @Singleton
//    ModelTenancyService modelTenancyService(
//            @Named(MODEL_TENANCY_TEMPLATE_LOADER) DocumentTemplateLoader templateLoader,
//            MetricRegistry metricRegistry) {
//        ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();
//        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
//        return new ModelTenancyService(documentGenerator, fieldExtractor, metricRegistry);
//    }


    @Provides
    @Singleton
    DocumentGenerationService<ModelTenancy> modelTenancyDocumentGenerationService(AsposeLicense asposeLicense,
                                                                                  MetricRegistry metricRegistry) {
        String templatePath = "/templates/model-tenancy-agreement-with-notes.docx";
        DocumentTemplateLoader templateLoader = new DocumentTemplateLoader(templatePath, asposeLicense);
        FieldExtractor<ModelTenancy> fieldExtractor = new ModelTenancyFieldExtractor();
        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
        DocumentGeneratorServiceListener<ModelTenancy> listener = new DocumentGeneratorServiceListenerAdaptor<>();
        IFieldMergingCallback fieldMergingCallback = new ModelTenancyMergingCallback(null);

        return  new DocumentGenerationService<>(
                documentGenerator,
                listener,
                fieldExtractor,
                fieldMergingCallback,
                metricRegistry);
    }

    @Provides
    RecaptchaCheck recaptchaCheck(HousingConfiguration configuration, Client client) {
        HousingConfiguration.Recaptcha recaptchaConfig = configuration.getRecaptcha();
        WebTarget verifyTarget = client.target(recaptchaConfig.RECAPTCHA_VERIFY_URL);
        return new RecaptchaCheck(
                recaptchaConfig.isEnabled(),
                verifyTarget,
                recaptchaConfig.getSecret());
    }

}
