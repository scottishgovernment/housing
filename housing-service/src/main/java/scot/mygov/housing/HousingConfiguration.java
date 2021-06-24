package scot.mygov.housing;

import java.io.File;
import java.net.URI;

public class HousingConfiguration {

    private int port = 8096;

    private URI geosearch = URI.create("http://localhost:9092/");

    private Aspose aspose = new Aspose();

    private Recaptcha recaptcha = new Recaptcha();

    private FairRentRegister fairRentRegister = new FairRentRegister();

    private URI cpiDataURI = URI.create("http://localhost:9200/housing-data/_doc/cpi/_source");

    private String europaId;

    private URI europaURI = URI.create("https://api.viaeuropa.uk.com/");

    // once per 5 minutes
    private long heartbeatMonitoringInterval = 5;

    private String cpiGracePeriod = "PT12H";

    // the date on which the new covid legislation comes into force.
    // this is used to switch the templates that are used for mta and notice to leave forms.
    private String covidChangeDate = "2020-10-01";

    private String changeDate2021 = "2021-03-30";

    public int getPort() {
        return port;
    }

    public double getPostcodeLookupResponseTimeThreshold() {
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

    public FairRentRegister getFairRentRegister() {
        return fairRentRegister;
    }

    public void setFairRentRegister(FairRentRegister fairRentRegister) {
        this.fairRentRegister = fairRentRegister;
    }

    public String getCpiGracePeriod() {
        return cpiGracePeriod;
    }

    public URI getCpiDataURI() { return cpiDataURI; }

    public String getEuropaId() {
        return europaId;
    }

    public void setEuropaId(String europaId) {
        this.europaId = europaId;
    }

    public URI getEuropaURI() {
        return europaURI;
    }

    public void setEuropaURI(URI europaURI) {
        this.europaURI = europaURI;
    }

    public long getHeartbeatMonitoringInterval() {
        return heartbeatMonitoringInterval;
    }

    public String getCovidChangeDate() {
        return covidChangeDate;
    }

    public void setCovidChangeDate(String covidChangeDate) {
        this.covidChangeDate = covidChangeDate;
    }

    public String getChangeDate2021() {
        return changeDate2021;
    }

    public void setChangeDate2021(String changeDate2021) {
        this.changeDate2021 = changeDate2021;
    }

    public static class Aspose {

        private File license;

        public File getLicense() {
            return license;
        }
    }

    public static class Recaptcha {
        public static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

        private boolean enabled = true;

        private String secret = "";

        private String sitekey = "";

        public boolean isEnabled() {
            return enabled;
        }

        public String getSecret() {
            return secret;
        }

        public String getSitekey() {
            return sitekey;
        }
    }

    public static class FairRentRegister {
        private URI uri = URI.create("https://fairrentapi.systems.gov.scot/");

        private String username;

        private String password;

        int connectTimeoutSeconds = 1;

        int readTimeoutSeconds = 10;

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getConnectTimeoutSeconds() {
            return connectTimeoutSeconds;
        }

        public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
            this.connectTimeoutSeconds = connectTimeoutSeconds;
        }

        public int getReadTimeoutSeconds() {
            return readTimeoutSeconds;
        }

        public void setReadTimeoutSeconds(int readTimeoutSeconds) {
            this.readTimeoutSeconds = readTimeoutSeconds;
        }
    }
}
