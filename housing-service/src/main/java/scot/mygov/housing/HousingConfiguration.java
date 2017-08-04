package scot.mygov.housing;

import java.io.File;
import java.net.URI;

public class HousingConfiguration {

    private int port = 8096;

    private URI geosearch = URI.create("http://localhost:9092/");

    private Aspose aspose = new Aspose();

    private URI cpiDataURI = URI.create("http://localhost:9200/housing-data/cpi/cpi/_source");

    private URI mapcloudURI = URI.create("https://api.themapcloud.com/");

    private String mapcloudUser = "";

    private String mapcloudPassword = "";

    private String cpiGracePeriod = "PT12H";

    public int getPort() {
        return port;
    }

    public URI getGeosearch() {
        return geosearch;
    }

    public Aspose getAspose() {
        return aspose;
    }

    public String getCpiGracePeriod() {
        return cpiGracePeriod;
    }

    public static class Aspose {

        private File license;

        public File getLicense() {
            return license;
        }
    }

    public URI getCpiDataURI() {
        return cpiDataURI;
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
}
