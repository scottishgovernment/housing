package scot.mygov.housing.forms.noticetoleave;

import jakarta.ws.rs.Path;
import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.noticetoleave.model.NoticeToLeave;

import javax.inject.Inject;

@Path("notice-to-leave")
public class NoticeToLeaveResource extends AbstractDocumentGenerationResource<NoticeToLeave> {

    @Inject
    public NoticeToLeaveResource(DocumentGenerationService<NoticeToLeave> service, RecaptchaCheck recaptchaCheck) {
        super(service, recaptchaCheck);
    }

    protected String contentDispositionFilenameStem() {
        return "notice-to-leave";
    }

    protected Class getModelClass() {
        return NoticeToLeave.class;
    }
}
