package scot.mygov.housing.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A client to call the Google Recaptcha service.
 */
public class RecaptchaCheck {

    private static final Logger LOG = LoggerFactory.getLogger(RecaptchaCheck.class);

    private final boolean enabled;
    private final WebTarget target;

    private final String secretKey;

    public RecaptchaCheck(boolean enabled, WebTarget target, String secretKey) {
        this.enabled = enabled;
        this.target = target;
        this.secretKey = secretKey;
    }

    /**
     * Given a response from the Google Recaptcha client the method will check so see if the response is valid.
     *
     * @param recaptchaClientResponse The response provided by the Google Recaptcha client.
     * @return true if the google recaptcha check passes.
     */
    public boolean verify(String recaptchaClientResponse) {

        // pass automatically if recaptcha is disabled
        if (!enabled) {
            return true;
        }

        String payload = String.format("secret=%s&response=%s", secretKey, recaptchaClientResponse);
        Entity entity = Entity.entity(payload, MediaType.APPLICATION_FORM_URLENCODED);
        Response response = target.request().post(entity);
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            return false;
        }

        // we cod a 200 range response code, read the entity
        RecaptchaResponse serverResponse = response.readEntity(RecaptchaResponse.class);
        if (serverResponse.isSuccess()) {
            // they passed
            return true;
        }

        // the test failed for some reason
        LOG.info("Recaptcha check failed {}", serverResponse.getErrorCodes());
        return false;
    }

}
