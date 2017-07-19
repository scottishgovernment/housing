package scot.mygov.housing;

import scot.mygov.housing.modeltenancy.ModelTenancyResource;

import javax.inject.Inject;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class HousingApplication extends Application {

    @Inject
    HousingResource housing;

    @Inject
    ModelTenancyResource modelTenancy;

    @Inject
    ErrorHandler errorHandler;

    @Inject
    Healthcheck healthcheck;

    @Inject
    RequestLogger logger;

    @Override
    public Set<Object> getSingletons() {
        return new HashSet<>(asList(
                housing,
                modelTenancy,
                errorHandler,
                healthcheck,
                logger
        ));
    }

}
