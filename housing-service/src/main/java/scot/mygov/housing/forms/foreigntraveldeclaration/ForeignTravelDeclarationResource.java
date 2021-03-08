package scot.mygov.housing.forms.foreigntraveldeclaration;

import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;
import scot.mygov.housing.forms.noticetoleave.model.NoticeToLeave;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

@Path("foreigntraveldeclaration")
public class ForeignTravelDeclarationResource extends AbstractDocumentGenerationResource<ForeignTravelDeclaration> {

    @Inject
    public ForeignTravelDeclarationResource(
            @Named("foreignTravelDeclarationDocumentGenerationService")
            DocumentGenerationService<ForeignTravelDeclaration> service,
            RecaptchaCheck recaptchaCheck) {
        super(service, recaptchaCheck);
    }

    protected String contentDispositionFilenameStem() {
        return "subtenant-notice-to-leave";
    }

    protected Class getModelClass() {
        return NoticeToLeave.class;
    }
}