package scot.mygov.housing.forms.modeltenancy.validation;

import scot.mygov.housing.forms.modeltenancy.model.Address;
import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.forms.modeltenancy.model.FurnishingType;
import scot.mygov.housing.forms.modeltenancy.model.Guarantor;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.model.RentPaymentFrequency;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ModelTenancyObjectMother {

    public ModelTenancy anyTenancy() {
        ModelTenancy tenancy = new ModelTenancy();
        tenancy.setTenants(singletonList(validPerson()));
        tenancy.setLettingAgent(anyAgentOrLandlord());
        tenancy.setLandlords(singletonList(anyAgentOrLandlord()));
        tenancy.setCommunicationsAgreement(CommunicationsAgreement.HARDCOPY.name());
        tenancy.setPropertyAddress(validPropertyAddress());
        tenancy.setPropertyType("FLAT");
        tenancy.setFurnishingType(FurnishingType.FURNISHED.name());
        tenancy.setRentPaymentFrequency(RentPaymentFrequency.CALENDAR_MONTH.name());
        tenancy.setTenancyStartDate(LocalDate.now());
        tenancy.setDepositAmount("100.00");
        tenancy.setRentAmount("200.00");
        tenancy.setFirstPaymentAmount("300.00");
        tenancy.setFirstPaymentDate(LocalDate.now());
        tenancy.setFirstPaymentPeriodEnd(LocalDate.now());
        tenancy.setRentPaymentDayOrDate("Monday");
        tenancy.setRentPaymentSchedule("Second Monday of each month");
        tenancy.setRentPaymentMethod("CASH");
        tenancy.setDepositAmount("300.00");
        tenancy.setTenancyDepositSchemeAdministrator("Letting Protection Service Scotland");
        return tenancy;
    }

    public ModelTenancy tenancyWithGuarentors() {
        ModelTenancy modelTenancy = anyTenancy();
        List<Guarantor> guarantors = modelTenancy.getTenants().stream().map(tenant -> {
            Guarantor guarantor = new Guarantor();
            guarantor.setName("guarentor name for " + tenant.getName());
            guarantor.setAddress(validAddress());
            guarantor.setTenantNames(singletonList(tenant.getName()));
            return guarantor;
        }).collect(toList());
        modelTenancy.setGuarantors(guarantors);
        return modelTenancy;
    }

    public ModelTenancy tenancyWithNoAgent() {
        ModelTenancy tenancy = anyTenancy();
        tenancy.setLettingAgent(null);
        return tenancy;
    }

    public ModelTenancy tenancyWithNoLandlords() {
        ModelTenancy tenancy = anyTenancy();
        tenancy.setLandlords(emptyList());
        return tenancy;
    }

    public ModelTenancy tenancyWithOneLandlord() {
        return anyTenancy();
    }

    public ModelTenancy tenancyWithTwoLandlords() {
        ModelTenancy tenancy = anyTenancy();
        List<AgentOrLandLord> landlords = new ArrayList<>();
        addAll(landlords, anyAgentOrLandlord(), anyAgentOrLandlord());
        tenancy.setLandlords(landlords);
        return tenancy;
    }

    public ModelTenancy tenancyWithThreeLandlords() {
        ModelTenancy tenancy = anyTenancy();
        List<AgentOrLandLord> landlords = new ArrayList<>();
        addAll(landlords, anyAgentOrLandlord(), anyAgentOrLandlord(), anyAgentOrLandlord());
        tenancy.setLandlords(landlords);
        return tenancy;
    }

    public AgentOrLandLord anyAgentOrLandlord() {
        AgentOrLandLord agentOrLandLord = new AgentOrLandLord();
        agentOrLandLord.setName("name");
        agentOrLandLord.setEmail("ddd@ddd.com");
        agentOrLandLord.setAddress(validAddress());
        agentOrLandLord.setTelephone("111");
        agentOrLandLord.setRegistrationNumber(validRegNumbers().get(0));
        return agentOrLandLord;
    }

    public List<Person> validPeople() {
        List<Person> people = new ArrayList<>();
        addAll(people,
                personWithAllFields(),
                personWithNullEmail(),
                personWithEmptyEmail(),
                personWithNullPhone(),
                personWithEmptyPhone()
        );
        return people;
    }

    public List<Person> invalidPeople() {
        List<Person> people = new ArrayList<>();
        addAll(people,
                emptyPerson(),
                personWithNoContactInfo(),
                personWithInvalidEmail()
        );
        return people;
    }

    public List<Address> validAddresses() {
        List<Address> addresses = new ArrayList<Address>();
        addAll(addresses,
                oneLineAddress(),
                partialDataAddress(),
                allDataAddress());
        return addresses;
    }

    public List<Address> invalidAddresses() {
        List<Address> addresses = new ArrayList<Address>();
        addAll(addresses,
                null,
                zeroLineAddress(),
                invalidPostcodeAddress(),
                noPostcodeAddress());
        return addresses;
    }

    public Address invalidPostcodeAddress() {
        Address address = validAddress();
        address.setPostcode("ZZZ");
        return address;
    }

    public Address noPostcodeAddress() {
        Address address = validAddress();
        address.setPostcode("");
        return address;
    }

    public Address zeroLineAddress() {
        Address address = validAddress();
        address.setBuilding("");
        address.setStreet("");
        address.setTown("");
        address.setRegion("");
        address.setPostcode("");
        return address;
    }

    public Address validAddress() {
        return allDataAddress();
    }

    public Address validAddress(String prefix) {
        return allDataAddress(prefix);
    }

    public String validPropertyAddress() {
        return "111 Royal Mile\n" +
                "Edinburgh" +
                "EH10 4BT";
    }

    public String validAddressFormatted() {
        return "(1) name, Dunroamin, 21 Some random street, Midlothian, Penicuik, EH104AX";
    }

    public Address oneLineAddress() {
        Address address = new Address();
        address.setStreet("21 Some random street");
        address.setPostcode(validPostcode());
        return address;
    }

    public Address partialDataAddress() {
        Address address = new Address();
        address.setStreet("21 Some random street");
        address.setTown("Randomtown");
        address.setPostcode(validPostcode());
        return address;
    }

    public Address allDataAddress() {
        Address address = new Address();
        address.setBuilding("Dunroamin");
        address.setStreet("21 Some random street");
        address.setTown("Penicuik");
        address.setRegion("Midlothian");
        address.setPostcode(validPostcode());
        return address;
    }

    public Address allDataAddress(String prefix) {
        Address address = new Address();
        address.setBuilding(prefix + "Dunroamin");
        address.setStreet(prefix + "21 Some random street");
        address.setTown(prefix + "Penicuik");
        address.setRegion(prefix + "Midlothian");
        address.setPostcode(validPostcode());
        return address;
    }
    public Person validPerson() {
        return personWithAllFields();
    }

    public Person personWithAllFields() {
        Person person = new Person();
        person.setName("name");
        person.setEmail("ddd@ddd.com");
        person.setAddress(validAddress());
        person.setTelephone("111");
        return person;
    }

    public Person personWithNullPhone() {
        Person person = personWithAllFields();
        person.setTelephone(null);
        return person;
    }

    public Person personWithEmptyPhone() {
        Person person = personWithAllFields();
        person.setTelephone("");
        return person;
    }

    public Person personWithNullEmail() {
        Person person = personWithAllFields();
        person.setEmail(null);
        return person;
    }

    public Person personWithEmptyEmail() {
        Person person = personWithAllFields();
        person.setEmail("");
        return person;
    }

    public Person personWithInvalidEmail() {
        Person person = personWithAllFields();
        person.setEmail(invalidEmails().get(0));
        return person;
    }

    public Person personWithNoContactInfo() {
        Person person = personWithAllFields();
        person.setEmail(null);
        person.setTelephone(null);
        return person;
    }

    public Person emptyPerson() {
        Person person = new Person();
        return person;
    }

    public List<String> invalidEmails() {
        List<String> emails = new ArrayList<>();
        addAll(emails, "asd", "", null, "@gmail.com", "google@ggogle@google", "http://www.google.com/");
        return emails;
    }

    public List<String> validEmails() {
        List<String> emails = new ArrayList<>();
        addAll(emails, "david.sinclait@gov.scot", "someone@blah.blah.blah");
        return emails;
    }

    public List<String> invalidRegNumbers() {
        List<String> registrationNumbers = new ArrayList<>();
        addAll(registrationNumbers, null, "", "aaa", "1111111/222/1111111", "11111111111111111111");
        return registrationNumbers;
    }

    public List<String> validRegNumbers() {
        List<String> registrationNumbers = new ArrayList<>();
        addAll(registrationNumbers, "666666/333/55555");
        return registrationNumbers;
    }

    public String validPostcode() {
        return "EH104AX";
    }

}
