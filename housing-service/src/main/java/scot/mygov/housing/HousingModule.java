package scot.mygov.housing;

import com.aspose.words.License;
import dagger.Module;
import dagger.Provides;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.config.Configuration;
import scot.mygov.housing.modeltenancy.ModelTenancyDocumentTemplateLoader;
import scot.mygov.housing.modeltenancy.ModelTenancyFieldExtractor;
import scot.mygov.housing.modeltenancy.ModelTenancyJsonTemplateLoader;
import scot.mygov.housing.modeltenancy.ModelTenancyService;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ModelTenancyValidatorFactory;
import scot.mygov.housing.rpz.InMemoryRPZService;
import scot.mygov.housing.rpz.PostcodeSource;
import scot.mygov.housing.rpz.RPZ;
import scot.mygov.housing.rpz.RPZService;
import scot.mygov.validation.Validator;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.time.LocalDate;
import java.util.Collections;

@Module(injects = Housing.class)
public class HousingModule {

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
    WebTarget target(Client client, HousingConfiguration configuration) {
        return client.target(configuration.getPostcodesUrl());
    }

    @Provides
    @Singleton
    Client client() {
        return new ResteasyClientBuilder().connectionPoolSize(10).build();
    }

    @Provides
    @Singleton
    PostcodeSource postcodeSource(WebTarget target) {
        return new PostcodeSource(target);
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
        return new ModelTenancyValidatorFactory().newInstance();
    }

    @Provides
    ModelTenancyService modelTenancyService() {
        // load the license
        License license = new License();
        // TODO: read from a url
        try {
            license.setLicense(Housing.class.getResourceAsStream("/Aspose.Words.lic"));
        } catch (Exception e) {
            LOG.error("Failed to load aspose license", e);
        }

        return new ModelTenancyService(
                new ModelTenancyDocumentTemplateLoader(configuration().getModelTenancyTemplatePath()),
                new ModelTenancyFieldExtractor(),
                new ModelTenancyJsonTemplateLoader()
        );
    }


}
