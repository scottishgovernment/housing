package scot.mygov.housing.modeltenancy;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import scot.mygov.housing.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.Guarantor;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.housing.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.housing.modeltenancy.model.Utility;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * Extract fields from a ModelTenancy object for use in a template.
 */
public class ModelTenancyFieldExtractor {

    private static final String NEWLINE = "\n";
    private static final String NOT_APPLICABLE = "n/a";
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public Map<String, Object> extractFields(ModelTenancy modelTenancy) {
        Map<String, Object> fields = new HashMap<>();

        extractTenants(modelTenancy, fields);
        extractGuarentorSignatureblock(modelTenancy, fields);
        extractLettingAgent(modelTenancy, fields);
        extractLandlords(modelTenancy, fields);
        extractCommunicationAgreement(modelTenancy, fields);

        fields.put("propertyAddress", modelTenancy.getPropertyAddress());
        fields.put("propertyType", modelTenancy.getPropertyType());
        fields.put("propertyIncludedAreasOrFacilities",
                commaSeparated(modelTenancy.getIncludedAreasOrFacilities()));
        fields.put("propertySharedFacilities",
                commaSeparated(modelTenancy.getSharedFacilities()));
        fields.put("propertyExcludedAreasFacilities",
                commaSeparated(modelTenancy.getExcludedAreasFacilities()));
        fields.put("furnishingType", modelTenancy.getFurnishingType().toLowerCase());

        if (modelTenancy.isInRentPressureZone()) {
            fields.put("rentPressureZoneString", "is");
        }

        if (modelTenancy.isHmoProperty()) {
            fields.put("hmoString", "is");
            fields.put("hmoContactNumber", modelTenancy.getHmo24ContactNumber());
            fields.put("hmoExpiryDate", dateFormatter.format(modelTenancy.getHmoRegistrationExpiryDate()));
        }

        fields.put("tenancyStartDate", dateFormatter.format(modelTenancy.getTenancyStartDate()));
        fields.put("rentAmount", modelTenancy.getRentAmount());
        fields.put("rentPaymentFrequency",
                RentPaymentFrequency.valueOf(modelTenancy.getRentPaymentFrequency()).getDescription());
        String advanceOrArrears = modelTenancy.isRentPayableInAdvance() ? "advance" : "arrears";
        fields.put("advanceOrArrears", advanceOrArrears);
        fields.put("firstPaymentDate", dateFormatter.format(modelTenancy.getFirstPaymentDate()));
        fields.put("firstPaymentAmount", modelTenancy.getFirstPaymentAmount());
        fields.put("firstPaymentPeriodStart", dateFormatter.format(modelTenancy.getFirstPaymentPeriodStart()));
        fields.put("firstPaymentPeriodEnd", dateFormatter.format(modelTenancy.getFirstPaymentPeriodEnd()));
        fields.put("rentPaymentDay", WordUtils.capitalize(modelTenancy.getRentPaymentDay().toLowerCase()));
        fields.put("rentPaymentMethod", modelTenancy.getRentPaymentMethod());
        fields.put("servicesIncludedInRent", naForEmpty(modelTenancy.getServicesIncludedInRent()));
        fields.put("depositAmount", modelTenancy.getDepositAmount());
        fields.put("depositSchemeAdministrator", modelTenancy.getTenancyDepositSchemeAdministrator());
        fields.put("depositSchemeContactDetails", modelTenancy.getTenancyDepositSchemeContactDetails());
        fields.put("tenantUtilitiesResponsibility",
                modelTenancy.getTenantUtilitiesResponsibilities()
                        .stream()
                        .map(utility -> Utility.valueOf(utility).getDescription())
                        .collect(joining(" / ")));

        return fields;
    }

    private String commaSeparated(List<String> list) {
        return list.stream().collect(Collectors.joining(", "));
    }

    private void extractTenants(ModelTenancy modelTenancy, Map<String, Object> fields) {
        List<String> names = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        List<String> phones = new ArrayList<>();
        IntStream.range(1, modelTenancy.getTenants().size() + 1).forEach(i -> {
            Person tenant = modelTenancy.getTenants().get(i - 1);
            names.add(nameAndAddress(tenant, i));
            emails.add(numberedValue(tenant.getEmail(), i));
            phones.add(numberedValue(tenant.getTelephone(), i));
        });
        fields.put("tenantNameAndAddresses", names.stream().collect(joining(NEWLINE)));
        fields.put("tenantEmails", emails.stream().collect(joining(NEWLINE)));
        fields.put("tenantPhoneNumbers", phones.stream().collect(joining(NEWLINE)));
        extractTenantSignatureblock(modelTenancy, fields);
    }

    private void extractTenantSignatureblock(ModelTenancy modelTenancy, Map<String, Object> fields) {
        List<String> signatureBlocks = new ArrayList<>();
        IntStream.range(1, modelTenancy.getTenants().size() + 1).forEach(i -> {
            Person tenant = modelTenancy.getTenants().get(i - 1);
            List<String> parts = new ArrayList<String>();
            Collections.addAll(parts,
                String.format("Tenant %d Signature:", i),
                String.format("Tenant Full Name:\t %s", tenant.getName()),
                String.format("Tenant Address Name:\t %s", tenant.getAddress()),
                "Date: ");
            signatureBlocks.add(parts.stream().collect(joining("\n")));
        });
        fields.put("tenantSignatures", signatureBlocks.stream().collect(joining("\n\n")));
    }

    private void extractGuarentorSignatureblock(ModelTenancy modelTenancy, Map<String, Object> fields) {
        List<String> signatureBlocks = new ArrayList<>();
        IntStream.range(1, modelTenancy.getGuarantors().size() + 1).forEach(i -> {
            Guarantor guarantor = modelTenancy.getGuarantors().get(i - 1);
            List<String> parts = new ArrayList<String>();
            String tenantNames = guarantor.getTenantNames().stream().collect(joining(", "));
            Collections.addAll(parts,
                String.format("Guarantor %d", i),
                String.format("Name(s) of Tenant(s) for whom Guarantor 1 will act as Guarantor:\n\t%s", tenantNames),
                String.format("Guarantor %d Signature:\t", i),
                String.format("Guarantor Full Name (Block  Capitals):\t%s", guarantor.getName().toUpperCase()),
                String.format("Guarantor Address:\t%s", guarantor.getAddress()),
                "Date:\t");
            signatureBlocks.add(parts.stream().collect(joining("\n")));
        });
        fields.put("guarentorSignatures", signatureBlocks.stream().collect(joining("\n\n")));
    }

    private void extractLandlords(ModelTenancy modelTenancy, Map<String, Object> fields) {
        IntStream.range(1, modelTenancy.getLandlords().size() + 1).forEach(i -> {
            AgentOrLandLord landlord = modelTenancy.getLandlords().get(i - 1);
            fields.put("landlordName"+i, landlord.getName());
            fields.put("landlordAddress"+i, landlord.getAddress());
            fields.put("landlordEmail"+i, numberedValue(landlord.getEmail(), i));
            fields.put("landlordPhone"+i, numberedValue(landlord.getTelephone(), i));
            fields.put("landlordRegistrationNumber"+i, landlord.getRegistrationNumber());
        });
    }

    private void extractLettingAgent(ModelTenancy modelTenancy, Map<String, Object> fields) {

        // letting agent is optional
        if (modelTenancy.getLettingAgent() == null) {
            return;
        }

        AgentOrLandLord agent = modelTenancy.getLettingAgent();
        fields.put("lettingAgentName", agent.getName());
        fields.put("lettingAgentAddress", agent.getAddress());
        fields.put("lettingAgentEmail", naForEmpty(agent.getEmail()));
        fields.put("lettingAgentPhone", naForEmpty(agent.getTelephone()));
        fields.put("lettingAgentRegistrationNumber", agent.getRegistrationNumber());
        fields.put("lettingAgentServices",
                naForEmpty(agent.getAgentServices().stream().collect(joining(", "))));
        fields.put("lettingAgentPointOfContact",
                naForEmpty(agent.getPointOfContact().stream().collect(joining(", "))));
    }

    private void extractCommunicationAgreement(ModelTenancy modelTenancy, Map<String, Object> fields) {
        if (modelTenancy.getCommunicationsAgreement().equals(CommunicationsAgreement.HARDCOPY.name())) {
            fields.put("communicationsAgreementHardcopy", "X");
            fields.put("communicationsAgreementEmail", "_");
            fields.put("communicationsAgreementType", "");
        }

        if (modelTenancy.getCommunicationsAgreement().equals(CommunicationsAgreement.EMAIL.name())) {
            fields.put("communicationsAgreementHardcopy", "_");
            fields.put("communicationsAgreementEmail", "X");
            fields.put("communicationsAgreementType", "email");
        }
    }

    private String nameAndAddress(Person tenant, int i) {
        return numberedValue(String.format("%s, \t%s", tenant.getName(), tenant.getAddress()), i);
    }

    private String numberedValue(String val, int i) {
        return String.format("(%d) %s", i, naForEmpty(val));
    }

    private String naForEmpty(String value) {
        return defaultForEmpty(value, NOT_APPLICABLE);
    }

    private String defaultForEmpty(String value, String defaultValue) {
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }
}