package scot.mygov.housing;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HousingConfiguration {

    private int port = 8096;

    private String modelTenancyTemplatePath = "/templates/model-tenancy-agreement.docx";

    private String postcodesUrl = "http://localhost:9092/postcodes/";

    private String asposeLicenseUrl;

    public HousingConfiguration() {
        // default location for Aspose license
        Path path = Paths.get(System.getProperty("user.home"), ".config/Aspose.Words.lic");
        asposeLicenseUrl = path.toFile().toURI().toString();
    }

    public int getPort() {
        return port;
    }

    public String getModelTenancyTemplatePath() {
        return modelTenancyTemplatePath;
    }

    public String getPostcodesUrl() {
        return postcodesUrl;
    }

    public String getAsposeLicenseUrl() {
        return asposeLicenseUrl;
    }
}
