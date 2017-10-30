package scot.mygov.housing;

import scot.mygov.housing.cpi.CPIResource;
import scot.mygov.housing.forms.modeltenancy.ModelTenancyResource;
import scot.mygov.housing.forms.nonprovisionofdocumentation.NonProvisionOfDocumentationResource;
import scot.mygov.housing.forms.rentadjudication.RentAdjudicationResource;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.postcode.PostcodeResource;
import scot.mygov.housing.rpz.RentPressureZoneResource;

import javax.inject.Inject;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class HousingApplication extends Application {

    @Inject
    RentPressureZoneResource rentPressureZone;

    @Inject
    ModelTenancyResource modelTenancy;

    @Inject
    RentAdjudicationResource rentAdjudication;

    @Inject
    NonProvisionOfDocumentationResource nonProvisionOfDocumentationResource;

    @Inject
    CPIResource cpiResource;

    @Inject
    PostcodeResource postcodeResource;

    @Inject
    ErrorHandler errorHandler;

    @Inject
    Healthcheck healthcheck;

    @Inject
    ResponseLogger responseLogger;

    @Inject
    RequestLogger requestLogger;

    @Inject
    Mapcloud mapcloud;

    @Override
    public Set<Object> getSingletons() {
        return new HashSet<>(asList(
                rentPressureZone,
                modelTenancy,
                rentAdjudication,
                nonProvisionOfDocumentationResource,
                cpiResource,
                postcodeResource,
                errorHandler,
                healthcheck,
                mapcloud,
                responseLogger,
                requestLogger
        ));
    }

}
