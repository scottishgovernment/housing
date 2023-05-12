package scot.mygov.housing.forms.rentincreasenotice;

import jakarta.ws.rs.Path;
import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.rentincreasenotice.model.RentIncrease;

import javax.inject.Inject;

@Path("rent-increase")
public class RentIncreaseResource extends AbstractDocumentGenerationResource<RentIncrease> {

    @Inject
    public RentIncreaseResource(DocumentGenerationService<RentIncrease> service, RecaptchaCheck recaptchaCheck) {
        super(service, recaptchaCheck);
    }
    protected String contentDispositionFilenameStem() {
        return "rent-increase";
    }

    protected Class getModelClass() {
        return RentIncrease.class;
    }
}
