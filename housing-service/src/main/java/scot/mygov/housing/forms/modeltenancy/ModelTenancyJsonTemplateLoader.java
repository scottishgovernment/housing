package scot.mygov.housing.forms.modeltenancy;

import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import javax.inject.Inject;

public class ModelTenancyJsonTemplateLoader {

    private final ModelTenancy modelTenancyTemplate;

    @Inject
    public ModelTenancyJsonTemplateLoader() {
        modelTenancyTemplate = new ModelTenancy();
        modelTenancyTemplate.setOptionalTerms(OptionalTermsUtil.defaultTerms());
    }

    public ModelTenancy loadJsonTemplate() {
        return modelTenancyTemplate;
    }

}
