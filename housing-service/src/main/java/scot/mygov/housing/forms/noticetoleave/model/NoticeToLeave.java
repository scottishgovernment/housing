package scot.mygov.housing.forms.noticetoleave.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import scot.mygov.housing.forms.AbstractFormModel;
import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NoticeToLeave extends AbstractFormModel {

    private List<String> tenantNames = new ArrayList<>();
    private Address address;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate entryDate;
    private List<AgentOrLandLord> landLords = new ArrayList<>();
    private AgentOrLandLord landlordsAgent = null;
    private List<String> reasons = new ArrayList<>();
    private String reasonDetails = "";
    private String supportingEvidence = "";
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate earliestTribunualDate;

    public List<String> getTenantNames() {
        return tenantNames;
    }

    public void setTenantNames(List<String> tenantNames) {
        this.tenantNames = tenantNames;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public List<AgentOrLandLord> getLandLords() {
        return landLords;
    }

    public void setLandLords(List<AgentOrLandLord> landLords) {
        this.landLords = landLords;
    }

    public AgentOrLandLord getLandlordsAgent() {
        return landlordsAgent;
    }

    public void setLandlordsAgent(AgentOrLandLord landlordsAgent) {
        this.landlordsAgent = landlordsAgent;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public String getReasonDetails() {
        return reasonDetails;
    }

    public void setReasonDetails(String reasonDetails) {
        this.reasonDetails = reasonDetails;
    }

    public String getSupportingEvidence() {
        return supportingEvidence;
    }

    public void setSupportingEvidence(String supportingEvidence) {
        this.supportingEvidence = supportingEvidence;
    }

    public LocalDate getEarliestTribunualDate() {
        return earliestTribunualDate;
    }

    public void setEarliestTribunualDate(LocalDate earliestTribunualDate) {
        this.earliestTribunualDate = earliestTribunualDate;
    }
}
