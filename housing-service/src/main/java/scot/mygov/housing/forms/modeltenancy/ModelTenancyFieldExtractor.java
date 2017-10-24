package scot.mygov.housing.forms.modeltenancy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.forms.modeltenancy.model.DepositSchemeAdministrator;
import scot.mygov.housing.forms.modeltenancy.model.DepositSchemeAdministrators;
import scot.mygov.housing.forms.modeltenancy.model.Guarantor;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;
import scot.mygov.housing.forms.modeltenancy.model.Service;
import scot.mygov.housing.forms.modeltenancy.model.Term;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Collections.addAll;
import static java.util.stream.Collectors.joining;
import static scot.mygov.housing.forms.FieldExtractorUtils.*;
import static scot.mygov.housing.forms.FieldExtractorUtils.addressFieldsMultipleLines;

/**
 * Extract fields from a ModelTenancy object for use in a template.
 */
public class ModelTenancyFieldExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ModelTenancyFieldExtractor.class);

    private static final String NEWLINE = "\n";

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private DepositSchemeAdministrators depositScemeAdministrators = new DepositSchemeAdministrators();

    @Inject
    public ModelTenancyFieldExtractor() {
        // Default constructor
    }

    public Map<String, Object> extractFields(ModelTenancy tenancy) {
        Map<String, Object> fields = new HashMap<>();

        // extract the fields - extract in the order they appear in the template for clarity.
        extractTenants(tenancy, fields);
        extractLettingAgent(tenancy, fields);
        extractLandlords(tenancy, fields);
        extractCommunicationAgreement(tenancy, fields);
        extractPropertyDetails(tenancy, fields);
        fields.put("tenancyStartDate", formatDate(tenancy.getTenancyStartDate()));
        extractRent(tenancy, fields);
        extractDeposit(tenancy, fields);
        extractOptionalTerms(tenancy.getOptionalTerms(), fields);
        extractAdditionalTerms(tenancy, fields);
        extractGuarantorSignatureblock(tenancy, fields);
        extractTenantSignatureblock(tenancy, fields);
        extractLandlordSignatureblock(tenancy, fields);

        return fields;
    }

    private void extractTenants(ModelTenancy tenancy, Map<String, Object> fields) {
        List<String> namesAndAddresses = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        List<String> phones = new ArrayList<>();

        for (int i = 0; i < tenancy.getTenants().size(); i++) {
            Person tenant = tenancy.getTenants().get(i);
            int tenantIndex = i + 1;
            namesAndAddresses.add(nameAndAddress(tenant, tenantIndex));
            emails.add(numberedValue(tenant.getEmail(), tenantIndex));
            phones.add(numberedValue(tenant.getTelephone(), tenantIndex));
        }

        fields.put("tenantNamesAndAddresses", namesAndAddresses.stream().collect(joining(NEWLINE)));
        fields.put("tenantEmails", emails.stream().collect(joining(NEWLINE)));
        fields.put("tenantPhoneNumbers", phones.stream().collect(joining(NEWLINE)));
        extractTenantSignatureblock(tenancy, fields);
    }

    private void extractLettingAgent(ModelTenancy tenancy, Map<String, Object> fields) {
        // letting agent is optional
        if (tenancy.getLettingAgent() == null) {
            fields.put("lettingAgentName", NOT_APPLICABLE);
            fields.put("lettingAgentAddress", NOT_APPLICABLE);
            fields.put("lettingAgentEmail", NOT_APPLICABLE);
            fields.put("lettingAgentPhone", NOT_APPLICABLE);
            fields.put("lettingAgentRegistrationNumber", NOT_APPLICABLE);
            return;
        }

        AgentOrLandLord agent = tenancy.getLettingAgent();
        fields.put("lettingAgentName", agent.getName());
        fields.put("lettingAgentAddress", addressFieldsMultipleLines(agent.getAddress()));
        fields.put("lettingAgentEmail", naForEmpty(agent.getEmail()));
        fields.put("lettingAgentPhone", naForEmpty(agent.getTelephone()));
        fields.put("lettingAgentRegistrationNumber", agent.getRegistrationNumber());
    }

    private void extractLandlords(ModelTenancy modelTenancy, Map<String, Object> fields) {
        List<String> landlordNames = new ArrayList<>();
        List<String> landlordAddresses = new ArrayList<>();
        List<String> landlordEmails = new ArrayList<>();
        List<String> landlordPhones = new ArrayList<>();
        List<String> landlordRegNumbers = new ArrayList<>();

        for (int i = 0; i < modelTenancy.getLandlords().size(); i++) {
            AgentOrLandLord landlord = modelTenancy.getLandlords().get(i);
            int landlordIndex = i + 1;
            landlordNames.add(String.format("Name (%d): %s", landlordIndex, landlord.getName()));
            landlordAddresses.add(String.format("Address (%d): %s%s%s",
                    landlordIndex, NEWLINE, addressFieldsMultipleLines(landlord.getAddress()), NEWLINE));
            landlordEmails.add(numberedValue(landlord.getEmail(), landlordIndex));
            landlordPhones.add(numberedValue(landlord.getTelephone(), landlordIndex));
            landlordRegNumbers.add(
                    String.format("Registration number (Landlord %d):  %s", landlordIndex, regNumber(landlord)));
        }
        fields.put("landlordNames", landlordNames.stream().collect(joining(NEWLINE)));
        fields.put("landlordAddresses", landlordAddresses.stream().collect(joining(NEWLINE)));
        fields.put("landlordEmails", landlordEmails.stream().collect(joining(NEWLINE)));
        fields.put("landlordPhones", landlordPhones.stream().collect(joining(NEWLINE)));
        fields.put("landlordRegNumbers", landlordRegNumbers.stream().collect(joining(NEWLINE + NEWLINE)));
    }

    private void extractCommunicationAgreement(ModelTenancy tenancy, Map<String, Object> fields) {

        if (Objects.isNull(tenancy.getCommunicationsAgreement())) {
            return;
        }

        if (tenancy.getCommunicationsAgreement().equals(CommunicationsAgreement.HARDCOPY.name())) {
            fields.put("communicationsAgreementHardcopy", "X");
            fields.put("communicationsAgreementEmail", " ");
        }

        if (tenancy.getCommunicationsAgreement().equals(CommunicationsAgreement.EMAIL.name())) {
            fields.put("communicationsAgreementHardcopy", " ");
            fields.put("communicationsAgreementEmail", "X");
        }
    }

    private void extractPropertyDetails(ModelTenancy tenancy, Map<String, Object> fields) {

        fields.put("propertyAddress", tenancy.getPropertyAddress());
        fields.put("propertyType", tenancy.getPropertyType());
        fields.put("furnishingType", tenancy.getFurnishingType().toLowerCase());
        if (tenancy.isInRentPressureZone()) {
            fields.put("rentPressureZoneString", "is");
        } else {
            fields.put("rentPressureZoneString", "is not");
        }
        if (tenancy.isHmoProperty()) {
            fields.put("hmoString", "is");
            fields.put("hmoContactNumber", tenancy.getHmo24ContactNumber());
            fields.put("hmoExpiryDate", formatDate(tenancy.getHmoRegistrationExpiryDate()));
        } else {
            fields.put("hmoString", "is not");
            fields.put("hmoContactNumber", NOT_APPLICABLE);
            fields.put("hmoExpiryDate", NOT_APPLICABLE);
        }

        extractServices(tenancy, fields);
        extractFacilities(tenancy, fields);
    }

    public void extractRent(ModelTenancy tenancy, Map<String, Object> fields) {
        fields.put("rentAmount", tenancy.getRentAmount());
        fields.put("rentPaymentFrequency", RentPaymentFrequency.description(tenancy.getRentPaymentFrequency()));
        String advanceOrArrears = tenancy.isRentPayableInAdvance() ? "advance" : "arrears";
        fields.put("advanceOrArrears", advanceOrArrears);
        fields.put("firstPaymentDate", formatDate(tenancy.getFirstPaymentDate()));
        fields.put("firstPaymentAmount", tenancy.getFirstPaymentAmount());
        fields.put("firstPaymentPeriodStart", formatDate(tenancy.getTenancyStartDate()));
        fields.put("firstPaymentPeriodEnd", formatDate(tenancy.getFirstPaymentPeriodEnd()));
        fields.put("rentPaymentDayOrDate", tenancy.getRentPaymentDayOrDate());
        fields.put("rentPaymentSchedule", tenancy.getRentPaymentSchedule());
        fields.put("rentPaymentMethod", tenancy.getRentPaymentMethod());
    }

    public void extractDeposit(ModelTenancy tenancy, Map<String, Object> fields) {
        fields.put("depositAmount", tenancy.getDepositAmount());
        fields.put("depositSchemeAdministrator", tenancy.getTenancyDepositSchemeAdministrator());
        DepositSchemeAdministrator depositSchemeAdministrator =
                depositScemeAdministrators.forName(tenancy.getTenancyDepositSchemeAdministrator());
        String depositSchemeAdministratorContactDetails = depositSchemeAdministratorContactDetails(depositSchemeAdministrator);
        fields.put("depositSchemeContactDetails", depositSchemeAdministratorContactDetails);
    }

    public void extractAdditionalTerms(ModelTenancy tenancy, Map<String, Object> fields) {
        if (tenancy.getAdditionalTerms().isEmpty()) {
            fields.put("additionalTerms", NOT_APPLICABLE);
            return;
        }

        String terms = tenancy.getAdditionalTerms().stream().map(this::formatTerm).collect(joining(NEWLINE));
        fields.put("additionalTerms", terms);
    }

    private String formatTerm(Term term) {
        return String.format("%s%s%s%s", term.getTitle(), NEWLINE, term.getContent(), NEWLINE);
    }

    private void extractServices(ModelTenancy tenancy, Map<String, Object> fields) {
        extractServicesList(tenancy.getServicesIncludedInRent(), fields, "servicesIncludedInRent");
        extractServicesList(tenancy.getServicesProvidedByLettingAgent(), fields, "lettingAgentServices");
        extractServicesList(tenancy.getServicesLettingAgentIsFirstContactFor(), fields, "lettingAgentPointOfContactServices");
    }

    private void extractServicesList(List<Service> services, Map<String, Object> fields, String field) {
        fields.put(field, services.stream().map(this::formatService).collect(joining(", ")));
    }

    private String formatService(Service service) {
        if (StringUtils.isEmpty(service.getValue())) {
            return service.getName();
        } else {
            return format("%s £%s", service.getName(), service.getValue());
        }
    }

    private void extractFacilities(ModelTenancy tenancy, Map<String, Object> fields) {
        fields.put("includedAreasOrFacilities",
                naForEmpty(tenancy.getIncludedAreasOrFacilities().stream().collect(joining(", "))));
        fields.put("excludedAreasFacilities",
                naForEmpty(tenancy.getExcludedAreasFacilities().stream().collect(joining(", "))));
        fields.put("sharedFacilities",
                naForEmpty(tenancy.getSharedFacilities().stream().collect(joining(", "))));
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

    private void extractGuarantorSignatureblock(ModelTenancy tenancy, Map<String, Object> fields) {
        List<String> signatureBlocks = new ArrayList<>();
        IntStream.range(1, tenancy.getGuarantors().size() + 1).forEach(i -> {
            Guarantor guarantor = tenancy.getGuarantors().get(i - 1);
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
        fields.put("guarentorSignatures", signatureBlocks.stream().collect(joining(NEWLINE + NEWLINE)));
    }

    private void extractTenantSignatureblock(ModelTenancy tenancy, Map<String, Object> fields) {
        List<String> signatureBlocks = new ArrayList<>();
        int index = 1;
        for (Person tenant : tenancy.getLandlords()) {
            List<String> parts = new ArrayList<String>();
            addAll(parts,
                    format("Tenant %d Full Name: %s", index, tenant.getName()),
                    format("Tenant %d Address:%s%s",
                            index, NEWLINE, addressFieldsMultipleLines(tenant.getAddress())),
                    format("Tenant %d Signature:", index),
                    format("Date:"),
                    NEWLINE);
            signatureBlocks.add(parts.stream().collect(joining(NEWLINE)));
            index++;
        }

        fields.put("tenantSignatures", signatureBlocks.stream().collect(joining(NEWLINE)));
    }

    private void extractLandlordSignatureblock(ModelTenancy tenancy, Map<String, Object> fields) {
        List<String> signatureBlocks = new ArrayList<>();
        int index = 1;
        for (AgentOrLandLord landLord : tenancy.getLandlords()) {
            List<String> parts = new ArrayList<String>();
            addAll(parts,
                    format("Landlord %d Full Name: %s", index, landLord.getName()),
                    format("Landlord %d Address:%s%s",
                            index, NEWLINE, addressFieldsMultipleLines(landLord.getAddress())),
                    format("Landlord %d Signature:", index),
                    format("Date:"),
                    NEWLINE);
            signatureBlocks.add(parts.stream().collect(joining(NEWLINE)));
            index++;
        }
        fields.put("landlordSignatures", signatureBlocks.stream().collect(joining(NEWLINE)));
    }

    private String regNumber(AgentOrLandLord landlord) {
        if (StringUtils.isEmpty(landlord.getRegistrationNumber())) {
            return "Pending – the Landlord will inform the Tenant of the Registration number once they have it";
        } else {
            return String.format("[%s]", landlord.getRegistrationNumber());
        }
    }

    private String depositSchemeAdministratorContactDetails(DepositSchemeAdministrator administrator) {
        if (administrator == null) {
            return "";
        }

        List<String> parts = new ArrayList<>();
        addAll(parts,
                administrator.getWebsite(),
                administrator.getEmail(),
                administrator.getTelephone());
        return parts.stream()
                .filter(StringUtils::isNotEmpty)
                .collect(joining("\n"));
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return dateFormatter.format(date);
    }

}