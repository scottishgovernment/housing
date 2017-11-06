package scot.mygov.housing;

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
import scot.mygov.housing.forms.modeltenancy.ModelTenancyFieldExtractor;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.rentadjudication.RentAdjudicationFieldExtractor;
import scot.mygov.housing.forms.rentadjudication.RentAdjudicationService;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyValidatorFactory;
import scot.mygov.housing.forms.modeltenancy.ModelTenancyService;
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

    private static final String MODEL_TENANCY_TEMPLATE_LOADER = "modelTenancyTemplateLoader";
    private static final String RENT_ADJUDICATION_TEMPLATE_LOADER = "rentAdjudicationTemplateLoader";

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
    Validator<ModelTenancy> modelTenancyValidator() {
        return new ModelTenancyValidatorFactory().validator(false);
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
    @Named(MODEL_TENANCY_TEMPLATE_LOADER)
    @Singleton
    DocumentTemplateLoader modelTenancyTemplateLoader(AsposeLicense asposeLicense) {
        return new DocumentTemplateLoader(scot.mygov.housing.forms.modeltenancy.ModelTenancyService.DOCUMENT_TEMPLATE_PATH, asposeLicense);
    }

    @Provides
    @Singleton
    ModelTenancyService modelTenancyService(
            @Named(MODEL_TENANCY_TEMPLATE_LOADER) DocumentTemplateLoader templateLoader,
            MetricRegistry metricRegistry) {
        ModelTenancyFieldExtractor fieldExtractor = new ModelTenancyFieldExtractor();
        DocumentGenerator documentGenerator = new DocumentGenerator(templateLoader);
        return new ModelTenancyService(documentGenerator, fieldExtractor, metricRegistry);
    }

    @Provides
    @Named(RENT_ADJUDICATION_TEMPLATE_LOADER)
    @Singleton
    DocumentTemplateLoader rentAdjudicationTemplateLoader(AsposeLicense asposeLicense) {
        return new DocumentTemplateLoader(RentAdjudicationService.DOCUMENT_TEMPLATE_PATH, asposeLicense);
    }

    @Provides
    @Singleton
    RentAdjudicationService rentAdjudicationService(
            @Named(RENT_ADJUDICATION_TEMPLATE_LOADER) DocumentTemplateLoader templateLoader) {
        RentAdjudicationFieldExtractor fieldExtractor = new RentAdjudicationFieldExtractor();
        return new RentAdjudicationService(templateLoader, fieldExtractor);
    }

    @Provides
    RecaptchaCheck recaptchaCheck(HousingConfiguration configuration, Client client) {
        HousingConfiguration.Recaptcha recaptchaConfig = configuration.getRecaptcha();
        return new RecaptchaCheck(
                recaptchaConfig.isEnabled(), client.target(recaptchaConfig.getUrl()), recaptchaConfig.getSecret());
    }

}
