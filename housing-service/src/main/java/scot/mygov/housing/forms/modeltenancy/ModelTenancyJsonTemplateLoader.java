package scot.mygov.housing.forms.modeltenancy;

import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import javax.inject.Inject;

public class ModelTenancyJsonTemplateLoader {

    private final ModelTenancy modelTenancyTemplate;

    @Inject
    public ModelTenancyJsonTemplateLoader() {
        modelTenancyTemplate = new ModelTenancy();
        modelTenancyTemplate.setOptionalTerms(TermsUtil.defaultOptionalTerms());
        modelTenancyTemplate.setMustIncludeTerms(TermsUtil.defaultMustIncludeTerms());
    }

    public ModelTenancy loadJsonTemplate() {
        return modelTenancyTemplate;
    }

}
