package scot.mygov.housing.forms.foreigntraveldeclaration;

import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("foreigntraveldeclaration")
public class ForeignTravelDeclarationResource extends AbstractDocumentGenerationResource<ForeignTravelDeclaration> {

    @Inject
    public ForeignTravelDeclarationResource(
            DocumentGenerationService<ForeignTravelDeclaration> service,
            RecaptchaCheck recaptchaCheck) {
        super(service, recaptchaCheck);
    }

    protected String contentDispositionFilenameStem() {
        return "travel-declaration";
    }

    protected Class getModelClass() {
        return ForeignTravelDeclaration.class;
    }
}