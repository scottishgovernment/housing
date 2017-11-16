package scot.mygov.housing.forms.rentadjudication;

import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("rent-adjudication")
public class RentAdjudicationResource extends AbstractDocumentGenerationResource<RentAdjudication> {

    @Inject
    public RentAdjudicationResource(DocumentGenerationService<RentAdjudication> service, RecaptchaCheck recaptchaCheck) {
        super(service, recaptchaCheck);
    }

    protected String contentDispositionFilenameStem() {
        return "adjudication";
    }

    protected Class getModelClass() {
        return RentAdjudication.class;
    }
}
