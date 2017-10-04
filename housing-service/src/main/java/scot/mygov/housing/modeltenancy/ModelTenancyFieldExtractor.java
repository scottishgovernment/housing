package scot.mygov.housing.modeltenancy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.modeltenancy.model.Address;
import scot.mygov.housing.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.DepositSchemeAdministrator;
import scot.mygov.housing.modeltenancy.model.Facility;
import scot.mygov.housing.modeltenancy.model.FacilityType;
import scot.mygov.housing.modeltenancy.model.Guarantor;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.OptionalTerms;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.housing.modeltenancy.model.RentPaymentFrequency;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Collections.addAll;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Extract fields from a ModelTenancy object for use in a template.
 */
public class ModelTenancyFieldExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ModelTenancyFieldExtractor.class);

    private static final String NEWLINE = "\n";
    private static final String NOT_APPLICABLE = "n/a";

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private DepositSchemeAdministrators depositScemeAdministrators = new DepositSchemeAdministrators();

    @Inject
    public ModelTenancyFieldExtractor() {
        // Default constructor
    }

    public Map<String, Object> extractFields(ModelTenancy modelTenancy) {
        Map<String, Object> fields = new HashMap<>();

        extractTenants(modelTenancy, fields);
        extractGuarentorSignatureblock(modelTenancy, fields);
        extractLettingAgent(modelTenancy, fields);
        extractLandlords(modelTenancy, fields);
        extractCommunicationAgreement(modelTenancy, fields);
        extractFacilities(modelTenancy, fields);
        fields.put("propertyAddress", addressFieldsMultipleLines(modelTenancy.getPropertyAddress()));
        fields.put("propertyType", modelTenancy.getPropertyType());
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
        fields.put("rentPaymentDayOrDate", modelTenancy.getRentPaymentDayOrDate());
        fields.put("rentPaymentSchedule", modelTenancy.getRentPaymentSchedule());
        fields.put("rentPaymentMethod", modelTenancy.getRentPaymentMethod());
        fields.put("depositAmount", modelTenancy.getDepositAmount());
        fields.put("depositSchemeAdministrator", modelTenancy.getTenancyDepositSchemeAdministrator());
        DepositSchemeAdministrator depositSchemeAdministrator =
                depositScemeAdministrators.forName(modelTenancy.getTenancyDepositSchemeAdministrator());
        String depositSchemeAdministratorContactDetails = depositSchemeAdministratorContactDetails(depositSchemeAdministrator);
        fields.put("depositSchemeContactDetails", depositSchemeAdministratorContactDetails);
        extractOptionalTerms(modelTenancy.getOptionalTerms(), fields);
        return fields;
    }

    private void extractFacilities(ModelTenancy modelTenancy, Map<String, Object> fields) {
        extractFacilitiesOfType(modelTenancy, fields, FacilityType.INCLUDED, "propertyIncludedAreasOrFacilities");
        extractFacilitiesOfType(modelTenancy, fields, FacilityType.EXCLUDED, "propertyExcludedAreasFacilities");
        extractFacilitiesOfType(modelTenancy, fields, FacilityType.SHARED, "propertySharedFacilities");
    }

    private void extractFacilitiesOfType(ModelTenancy modelTenancy, Map<String, Object> fields, FacilityType type, String key) {
        String facilitiesString = modelTenancy.getFacilities()
                .stream()
                .filter(f -> f.getType() == type)
                .map(Facility::getName)
                .collect(joining(", "));
        fields.put(key, facilitiesString);
    }

    private void extractOptionalTerms(OptionalTerms optionalTerms, Map<String, Object> fields) {
        try {
            BeanUtils.describe(optionalTerms)
                    .entrySet()
                    .stream()
                    .forEach(entry -> fields.put(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            LOG.warn("Failed to extract properties", e);
        }
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
            addAll(parts,
                format("Tenant %d Signature:", i),
                format("Tenant Full Name:\t %s", tenant.getName()),
                format("Tenant Address:\n %s", addressFieldsMultipleLines(tenant.getAddress())),
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
            addAll(parts,
                format("Guarantor %d", i),
                format("Name(s) of Tenant(s) for whom Guarantor 1 will act as Guarantor:\n\t%s", tenantNames),
                format("Guarantor %d Signature:\t", i),
                format("Guarantor Full Name (Block  Capitals):\t%s", guarantor.getName().toUpperCase()),
                format("Guarantor Address:\n%s", addressFieldsMultipleLines(guarantor.getAddress())),
                "Date:\t");
            signatureBlocks.add(parts.stream().collect(joining("\n")));
        });
        fields.put("guarentorSignatures", signatureBlocks.stream().collect(joining("\n\n")));
    }

    private void extractLandlords(ModelTenancy modelTenancy, Map<String, Object> fields) {
        IntStream.range(1, modelTenancy.getLandlords().size() + 1).forEach(i -> {
            AgentOrLandLord landlord = modelTenancy.getLandlords().get(i - 1);
            fields.put("landlordName"+i, landlord.getName());
            fields.put("landlordAddress"+i, addressFieldsMultipleLines(landlord.getAddress()));
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
        fields.put("lettingAgentAddress", addressFieldsMultipleLines(agent.getAddress()));
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

    private List<String> addressParts(Address address) {
        List<String> parts = new ArrayList<>();
        addAll(parts,
            address.getAddressLine1(),
            address.getAddressLine2(),
            address.getAddressLine3(),
            address.getPostcode());
        return parts.stream().filter(part -> isNotEmpty(part)).collect(toList());
    }

    private String addressFieldsMultipleLines(Address address) {
        return addressParts(address).stream().collect(joining(",\n"));
    }

    private String addressFieldsSingleLine(Address address) {
        return addressParts(address).stream().collect(joining(", "));
    }

    private String nameAndAddress(Person person, int i) {
        String address = addressFieldsSingleLine(person.getAddress());
        String nameAndAddress = format("%s, %s", person.getName(), address);
        return numberedValue(nameAndAddress, i);
    }

    private String numberedValue(String val, int i) {
        return format("(%d) %s", i, naForEmpty(val));
    }

    private String naForEmpty(String value) {
        return defaultForEmpty(value, NOT_APPLICABLE);
    }

    private String depositSchemeAdministratorContactDetails(DepositSchemeAdministrator administrator) {
        List<String> parts = new ArrayList<>();
        addAll(parts,
                administrator.getWebsite(),
                administrator.getEmail(),
                administrator.getTelephone());
        return parts.stream()
                .filter(StringUtils::isNotEmpty)
                .collect(joining("\n"));
    }

    private String defaultForEmpty(String value, String defaultValue) {
        if (isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }
}