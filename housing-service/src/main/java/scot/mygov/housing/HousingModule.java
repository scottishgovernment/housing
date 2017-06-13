package scot.mygov.housing;

import dagger.Module;
import dagger.Provides;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.config.Configuration;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

@Module(injects = Housing.class)
public class HousingModule {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(HousingConfiguration.class);

    private static final String APP_NAME = "housing";

    @Provides
    @Singleton
    HousingConfiguration configuration() {
        Configuration<HousingConfiguration> configuration = Configuration
                .load(new HousingConfiguration(), APP_NAME)
                .validate();
        LOGGER.info("{}", configuration);
        return configuration.getConfiguration();
    }

    @Provides
    @Singleton
    Client client() {
        return new ResteasyClientBuilder()
                .connectionPoolSize(10)
                .build();
    }

    @Provides
    WebTarget target(Client client, HousingConfiguration configuration) {
        return client.target(configuration.getIndex());
    }

}
