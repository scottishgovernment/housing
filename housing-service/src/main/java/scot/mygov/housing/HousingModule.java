package scot.mygov.housing;

import com.codahale.metrics.MetricRegistry;
import dagger.Module;
import dagger.Provides;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.config.Configuration;
import scot.mygov.housing.cpi.CPIService;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ModelTenancyValidatorFactory;
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

    public static final String MAPCLOUD_TARGET = "mapcloudTarget";
    public static final String ELASTICSEARCH_TARGET = "esTarget";

    private static final Logger LOG = LoggerFactory.getLogger(HousingConfiguration.class);

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
}
