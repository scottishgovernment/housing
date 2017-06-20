package scot.mygov.housing.modeltenancy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z418868 on 14/06/2017.
 */
public class AgentOrLandLord extends Person {

    private String registrationNumber;
    private List<String> agentServices = new ArrayList<>();
    private List<String> pointOfContact = new ArrayList<>();

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public List<String> getAgentServices() {
        return agentServices;
    }

    public void setAgentServices(List<String> agentServices) {
        this.agentServices = agentServices;
    }

    public List<String> getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(List<String> pointOfContact) {
        this.pointOfContact = pointOfContact;
    }
}
