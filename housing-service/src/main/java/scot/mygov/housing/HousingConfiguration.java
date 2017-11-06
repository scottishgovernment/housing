package scot.mygov.housing;

import java.io.File;
import java.net.URI;

public class HousingConfiguration {

    private int port = 8096;

    private URI geosearch = URI.create("http://localhost:9092/");

    private Aspose aspose = new Aspose();

    private Recaptcha recaptcha = new Recaptcha();

    private URI cpiDataURI = URI.create("http://localhost:9200/housing-data/cpi/cpi/_source");

    private URI rpzDataURI = URI.create("http://localhost:9200/housing-data/rpz/_search/template");

    private URI rpzHealthURI = URI.create("http://localhost:9200/housing-data/rpz/_search");

    private URI mapcloudURI = URI.create("https://api.themapcloud.com/");

    private String mapcloudUser = "";

    private String mapcloudPassword = "";

    // once per 5 minutes
    private long mapcloudMonitoringInterval = 5;

    private String cpiGracePeriod = "PT12H";

    public int getPort() {
        return port;
    }

    public double getMapcloudResponseTimeThreshold() {
        return 500;
    }

    public URI getGeosearch() {
        return geosearch;
    }

    public Aspose getAspose() {
        return aspose;
    }

    public Recaptcha getRecaptcha() {
        return recaptcha;
    }

    public String getCpiGracePeriod() {
        return cpiGracePeriod;
    }

    public URI getCpiDataURI() { return cpiDataURI; }

    public URI getRpzDataURI() { return rpzDataURI; }

    public URI getRpzHealthURI() {
        return rpzHealthURI;
    }

    public URI getMapcloudURI() {
        return mapcloudURI;
    }

    public String getMapcloudUser() {
        return mapcloudUser;
    }

    public String getMapcloudPassword() {
        return mapcloudPassword;
    }

    public long getMapcloudMonitoringInterval() {
        return mapcloudMonitoringInterval;
    }

    public static class Aspose {

        private File license;

        public File getLicense() {
            return license;
        }
    }

    public static class Recaptcha {
        private boolean enabled = true;

        private String secret = "";

        private String url = "https://www.google.com/recaptcha/api/siteverify";

        public boolean isEnabled() {
            return enabled;
        }

        public String getSecret() {
            return secret;
        }

        public String getUrl() {
            return url;
        }
    }
}
