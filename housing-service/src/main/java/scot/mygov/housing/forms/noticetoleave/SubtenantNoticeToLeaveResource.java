package scot.mygov.housing.forms.noticetoleave;

import scot.mygov.housing.forms.AbstractDocumentGenerationResource;
import scot.mygov.housing.forms.DocumentGenerationService;
import scot.mygov.housing.forms.RecaptchaCheck;
import scot.mygov.housing.forms.noticetoleave.model.NoticeToLeave;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

@Path("subtenant-notice-to-leave")
public class SubtenantNoticeToLeaveResource extends AbstractDocumentGenerationResource<NoticeToLeave> {

    @Inject
    public SubtenantNoticeToLeaveResource(
            @Named("subtenantNoticeToLeaveDocumentGenerationService")
            DocumentGenerationService<NoticeToLeave> service,
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
