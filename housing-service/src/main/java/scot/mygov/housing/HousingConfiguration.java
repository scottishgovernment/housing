package scot.mygov.housing;

import java.io.File;
import java.net.URI;

public class HousingConfiguration {

    private int port = 8096;

    private URI geosearch = URI.create("http://localhost:9092/");

    private Aspose aspose = new Aspose();

    public HousingConfiguration() {
    }

    public int getPort() {
        return port;
    }

    public URI getGeosearch() {
        return geosearch;
    }

    public Aspose getAspose() {
        return aspose;
    }

    public static class Aspose {

        private File license;

        public File getLicense() {
            return license;
        }
    }

}
