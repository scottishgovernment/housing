package scot.mygov.housing;

import com.codahale.metrics.MetricRegistry;
import dagger.Module;
import dagger.Provides;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.config.Configuration;
import scot.mygov.housing.cpi.CPIService;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ModelTenancyValidatorFactory;
import scot.mygov.housing.postcode.PostcodeService;
import scot.mygov.housing.postcode.mapcloud.MapcloudPostcodeService;
import scot.mygov.housing.rpz.InMemoryRPZService;
import scot.mygov.housing.rpz.PostcodeSource;
import scot.mygov.housing.rpz.RPZ;
import scot.mygov.housing.rpz.RPZService;
import scot.mygov.validation.Validator;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;

@Module(injects = Housing.class)
public class HousingModule {

    public static final String GEO_HEALTH = "geosearch-health";

    public static final String GEO_POSTCODES = "geosearch-postcodes";

    public static final String MAPCLOUD = "mapcloud";

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
    @Named(GEO_HEALTH)
    WebTarget geosearchHealth(Client client, HousingConfiguration configuration) {
        return client.target(appendPath(configuration.getGeosearch(), "health"));
    }

    @Provides
    @Named(GEO_POSTCODES)
    WebTarget geosearchPostcode(Client client, HousingConfiguration configuration) {
        return client.target(appendPath(configuration.getGeosearch(), "postcodes"));
    }

    @Provides
    @Named(MAPCLOUD)
    WebTarget mapcloud(Client client, HousingConfiguration configuration) {
        return client.target(configuration.getMapcloudURI());
    }

    @Provides
    @Singleton
    Client client() {
        return new ResteasyClientBuilder().connectionPoolSize(10).build();
    }

    @Provides
    RPZService rpzService(PostcodeSource postcodeSource) {
        RPZ rpz = new RPZ("Davids flat",
                LocalDate.of(2016, 1, 1),
                LocalDate.of(2017, 1, 1), 1,
                Collections.singleton("EH104AX"));
        return new InMemoryRPZService(Collections.singleton(rpz), postcodeSource);
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
    PostcodeService postcodeService(
            HousingConfiguration config,
            @Named(MAPCLOUD) WebTarget mapcloud,
            PostcodeSource postcodeSource,
            MetricRegistry registry) {
        return new MapcloudPostcodeService(
                mapcloud,
                config.getMapcloudUser(),
                config.getMapcloudPassword(),
                registry);
    }

    @Provides
    @Singleton
    MetricRegistry metricsRegistry() {
        return new MetricRegistry();
    }

    static URI appendPath(URI uri, String path) {
        try {
            return new URI(uri.getScheme(),
                    uri.getUserInfo(),
                    uri.getHost(),
                    uri.getPort(),
                    Paths.get(uri.getPath(), path).toString(),
                    uri.getQuery(),
                    uri.getFragment());
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Could not append path " + path + " to " + uri, ex);
        }
    }

}
