package scot.mygov.housing;

public class HousingConfiguration {

    private int port = 8096;

    private String modelTenancyTemplatePath = "/templates/model-tenancy-agreement.docx";

    public int getPort() {
        return port;
    }

    public String getModelTenancyTemplatePath() {
        return modelTenancyTemplatePath;
    }
}
