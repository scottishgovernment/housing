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

import scot.mygov.housing.forms.PlaceholderProvidingMergingCallback;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.DocumentGenerationService;

import scot.mygov.housing.forms.modeltenancy.ModelTenancyFieldExtractor;
import scot.mygov.housing.forms.modeltenancy.ModelTenancyMergingCallback;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import scot.mygov.housing.forms.nonprovisionofdocumentation.NonProvisionOfDocumentationFieldExtractor;
import scot.mygov.housing.forms.nonprovisionofdocumentation.NonProvisionOfDocumentationPlaceholders;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;

import scot.mygov.housing.forms.noticetoleave.NoticeToLeaveFieldExtractor;
import scot.mygov.housing.forms.noticetoleave.NoticeToLeavePlaceholders;
import scot.mygov.housing.forms.noticetoleave.model.NoticeToLeave;

import scot.mygov.housing.forms.rentadjudication.RentAdjudicationFieldExtractor;
import scot.mygov.housing.forms.rentadjudication.RentAdjudicationPlaceholders;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;

import scot.mygov.housing.forms.rentincreaseforimprovementsnotice.RentIncreaseForImprovementsFieldExtractor;
import scot.mygov.housing.forms.rentincreaseforimprovementsnotice.RentIncreaseForImprovementsPlaceholders;
import scot.mygov.housing.forms.rentincreaseforimprovementsnotice.model.RentIncreaseForImprovements;
import scot.mygov.housing.forms.rentincreasenotice.RentIncreaseFieldExtractor;
import scot.mygov.housing.forms.rentincreasenotice.RentIncreasePlaceholders;
import scot.mygov.housing.forms.rentincreasenotice.model.RentIncrease;

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
    @Singleton
    DocumentGenerationService<ModelTenancy> modelTenancyDocumentGenerationService(AsposeLicense asposeLicense,
                                                                                  MetricRegistry metricRegistry) {
        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/model-tenancy-agreement-with-notes.docx", asposeLicense);
        return  new DocumentGenerationService<>(
                new DocumentGenerator(templateLoader),
                new ModelTenancyFieldExtractor(),
                form -> new ModelTenancyMergingCallback(form),
                metricRegistry);
    }

    @Provides
    DocumentGenerationService<RentAdjudication> rentAdjudicationDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/rent-adjudication.docx", asposeLicense);
        return new DocumentGenerationService<>(
                    new DocumentGenerator(templateLoader),
                    new RentAdjudicationFieldExtractor(),
                    form -> new PlaceholderProvidingMergingCallback(RentAdjudicationPlaceholders.placeholders()),
                    metricRegistry);
    }

    @Provides
    DocumentGenerationService<RentIncrease> rentIncreaseDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/rent-increase.docx", asposeLicense);
        return  new DocumentGenerationService<>(
                new DocumentGenerator(templateLoader),
                new RentIncreaseFieldExtractor(),
                form -> new PlaceholderProvidingMergingCallback(RentIncreasePlaceholders.placeholders()),
                metricRegistry);
    }

    @Provides
    DocumentGenerationService<RentIncreaseForImprovements> rentIncreaseForImprovementsDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/rent-increase-for-improvements.docx", asposeLicense);
        return  new DocumentGenerationService<>(
                new DocumentGenerator(templateLoader),
                new RentIncreaseForImprovementsFieldExtractor(),
                form -> new PlaceholderProvidingMergingCallback(RentIncreaseForImprovementsPlaceholders.placeholders()),
                metricRegistry);
    }

    @Provides
    DocumentGenerationService<NonProvisionOfDocumentation> nonProvisionOfDocumentationDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/non-provision-of-documentation.docx", asposeLicense);
        return  new DocumentGenerationService<>(
                    new DocumentGenerator(templateLoader),
                    new NonProvisionOfDocumentationFieldExtractor(),
                    form -> new PlaceholderProvidingMergingCallback(NonProvisionOfDocumentationPlaceholders.placeholders()),
                    metricRegistry);
    }

    @Provides
    DocumentGenerationService<NoticeToLeave> noticeToLeaveDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/notice-to-leave.docx", asposeLicense);
        return  new DocumentGenerationService<>(
                new DocumentGenerator(templateLoader),
                new NoticeToLeaveFieldExtractor(),
                form -> new PlaceholderProvidingMergingCallback(NoticeToLeavePlaceholders.placeholders()),
                metricRegistry);
    }

    @Named("subtenantNoticeToLeaveDocumentGenerationService")
    @Provides
    DocumentGenerationService<NoticeToLeave> subtenantNoticeToLeaveDocumentGenerationService(
            AsposeLicense asposeLicense,
            MetricRegistry metricRegistry) {

        DocumentTemplateLoader templateLoader
                = new DocumentTemplateLoader("/templates/subtenant-notice-to-leave.docx", asposeLicense);
        return  new DocumentGenerationService<>(
                new DocumentGenerator(templateLoader),
                new NoticeToLeaveFieldExtractor(),
                form -> new PlaceholderProvidingMergingCallback(NoticeToLeavePlaceholders.placeholders()),
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

    @Provides
    Validator<ModelTenancy> modelTenancyValidator() {
        return new ModelTenancyValidatorFactory().validator(false);
    }

}
