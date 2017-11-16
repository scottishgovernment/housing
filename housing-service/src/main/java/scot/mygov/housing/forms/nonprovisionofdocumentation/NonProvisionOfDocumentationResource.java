package scot.mygov.housing.forms.nonprovisionofdocumentation;

import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("non-provision-of-documentation")
public class NonProvisionOfDocumentationResource extends AbstractDocumentGenerationResource<NonProvisionOfDocumentation> {

    @Inject
    public NonProvisionOfDocumentationResource(
            DocumentGenerationService<NonProvisionOfDocumentation> service,
            RecaptchaCheck recaptchaCheck) {
        super(service, recaptchaCheck);
    }

    protected String contentDispositionFilenameStem() {
        return "non-provision-of-documentation";
    }

    protected Class getModelClass() {
        return NonProvisionOfDocumentation.class;
    }

}
