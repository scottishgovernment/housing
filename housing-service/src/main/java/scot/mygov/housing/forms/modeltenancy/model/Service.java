package scot.mygov.housing.forms.modeltenancy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    private String name;
    private String value;
    private String lettingAgentIsFirstContact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLettingAgentIsFirstContact() {
        return lettingAgentIsFirstContact;
    }

    public void setLettingAgentIsFirstContact(String lettingAgentIsFirstContact) {
        this.lettingAgentIsFirstContact = lettingAgentIsFirstContact;
    }
}