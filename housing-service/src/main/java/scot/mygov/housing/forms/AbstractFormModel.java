package scot.mygov.housing.forms;

public abstract class AbstractFormModel {

    private String recaptcha = "";

    public String getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(String recaptcha) {
        this.recaptcha = recaptcha;
    }
}
