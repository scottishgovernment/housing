package scot.mygov.housing;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("configuration")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @Inject
    HousingConfiguration housingConfiguration;

    @Inject
    ConfigResource() {
    }

    @GET
    public PublicConfig config() {
        PublicConfig config = new PublicConfig();
        config.setRecaptchaSitekey(housingConfiguration.getRecaptcha().getSitekey());
        return config;
    }

    class PublicConfig {

        String recaptchaSitekey;

        public String getRecaptchaSiteKey() {
            return recaptchaSitekey;
        }

        public void setRecaptchaSitekey(String recaptchaSitekey) {
            this.recaptchaSitekey = recaptchaSitekey;
        }
    }
}
