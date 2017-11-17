package scot.mygov.housing.forms.rentincreaseforimprovementsnotice;

import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.rentincreaseforimprovementsnotice.model.RentIncreaseForImprovements;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("rent-increase-for-improvements")
public class RentIncreaseForImprovementsResource extends AbstractDocumentGenerationResource<RentIncreaseForImprovements> {

    @Inject
    public RentIncreaseForImprovementsResource(DocumentGenerationService<RentIncreaseForImprovements> service, RecaptchaCheck recaptchaCheck) {
        super(service, recaptchaCheck);
    }
    protected String contentDispositionFilenameStem() {
        return "rent-increase-for-improvements";
    }

    protected Class getModelClass() {
        return RentIncreaseForImprovements.class;
    }
}
