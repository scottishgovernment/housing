package scot.mygov.housing;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.inject.Inject;

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
