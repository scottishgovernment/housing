package scot.mygov.housing.forms.modeltenancy;

import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import javax.inject.Inject;

public class ModelTenancyJsonTemplateLoader {

    @Inject
    public ModelTenancyJsonTemplateLoader() {
        // required so Dagger can inject this class
    }

    // built fresh on every call (rather than cached) so the legislation change date switch
    // takes effect without requiring a redeploy.
    public ModelTenancy loadJsonTemplate() {
        ModelTenancy modelTenancyTemplate = new ModelTenancy();
        modelTenancyTemplate.setOptionalTerms(TermsUtil.defaultOptionalTerms());
        modelTenancyTemplate.setMustIncludeTerms(TermsUtil.defaultMustIncludeTerms());
        return modelTenancyTemplate;
    }

}
