package scot.mygov.housing.forms.modeltenancy;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.validation.Validator;

import javax.inject.Inject;

@Path("model-tenancy")
public class ModelTenancyResource extends AbstractDocumentGenerationResource<ModelTenancy> {

    ModelTenancyJsonTemplateLoader jsonTemplateLoader;

    Validator<ModelTenancy> validator;

    @Inject
    public ModelTenancyResource(
            DocumentGenerationService<ModelTenancy> service,
            Validator<ModelTenancy> validator,
            RecaptchaCheck recaptchaCheck,
            ModelTenancyJsonTemplateLoader jsonTemplateLoader) {
        super(service, recaptchaCheck);
        this.validator = validator;
        this.jsonTemplateLoader = jsonTemplateLoader;
    }

    @GET
    @Path("template")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelTenancy modelTenancyTemplate() throws ModelTenancyServiceException {
        try {
            return jsonTemplateLoader.loadJsonTemplate();
        } catch (RuntimeException e) {
            throw new ModelTenancyServiceException("Failed to load model tenancy template", e);
        }
    }

    @Override
    protected void validate(ModelTenancy model) {
        validator.validate(model);
    }

    protected String contentDispositionFilenameStem() {
        return "tenancy";
    }

    protected Class getModelClass() {
        return ModelTenancy.class;
    }
}
