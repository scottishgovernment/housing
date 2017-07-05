package scot.mygov.housing;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HousingConfiguration {

    private int port = 8096;

    private String postcodesUrl = "http://localhost:9092/postcodes/";

    private String geoHealthUrl = "http://localhost:9092/health/";

    private URI asposeLicenseUri;

    public HousingConfiguration() {
        // default location for Aspose license
        Path path = Paths.get(System.getProperty("user.home"), ".config/Aspose.Words.lic");
        asposeLicenseUri = path.toFile().toURI();
    }

    public int getPort() {
        return port;
    }

    public String getPostcodesUrl() {
        return postcodesUrl;
    }

    public String getGeoHealthUrl() {
        return geoHealthUrl;
    }

    public URI getAsposeLicenseUri() {
        return asposeLicenseUri;
    }
}
