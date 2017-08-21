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
import scot.mygov.housing.rpz.InMemoryRPZService;
import scot.mygov.housing.rpz.RPZ;
import scot.mygov.housing.rpz.RPZService;
import scot.mygov.validation.Validator;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.time.LocalDate;

import static java.util.Collections.singleton;

@Module(injects = Housing.class)
public class HousingModule {

    public static final String MAPCLOUD_TARGET = "mapcloudTarget";

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
    @Singleton
    Client client() {
        return new ResteasyClientBuilder().connectionPoolSize(10).build();
    }

    @Provides
    RPZService rpzService(Mapcloud mapcloud) {
        RPZ rpz = new RPZ("Davids flat",
                LocalDate.of(2016, 1, 1),
                LocalDate.of(2017, 1, 1), 1,
                singleton("EH104AX"),
                singleton(""));
        return new InMemoryRPZService(singleton(rpz), mapcloud);
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
