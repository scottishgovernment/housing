package scot.mygov.housing.modeltenancy.validation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import scot.mygov.housing.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.FurnishingType;
import scot.mygov.housing.modeltenancy.model.Guarantor;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.model.Person;
import scot.mygov.housing.modeltenancy.model.RentPaymentFrequency;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by z418868 on 16/06/2017.
 */
public class ObjectMother {

    public ModelTenancy anyTenancy() {
        ModelTenancy tenancy = new ModelTenancy();
        tenancy.setTenants(Collections.singletonList(validPerson()));
        tenancy.setLettingAgent(anyAgentOrLandlord());
        tenancy.setLandlords(Collections.singletonList(anyAgentOrLandlord()));
        tenancy.setCommunicationsAgreement(CommunicationsAgreement.HARDCOPY.name());
        tenancy.setPropertyAddress("111 property address ");
        tenancy.setPropertyType("FLAT");
        tenancy.setFurnishingType(FurnishingType.FURNISHED.name());
        tenancy.setRentPaymentFrequency(RentPaymentFrequency.CALENDAR_MONTH.name());
        tenancy.setTenancyStartDate(LocalDate.now());
        tenancy.setDepositAmount("100.00");
        tenancy.setRentAmount("200.00");
        tenancy.setFirstPaymentAmount("300.00");
        tenancy.setFirstPaymentDate(LocalDate.now());
        tenancy.setFirstPaymentPeriodStart(LocalDate.now());
        tenancy.setFirstPaymentPeriodEnd(LocalDate.now());
        tenancy.setRentPaymentDay("MONDAY");
        tenancy.setRentPaymentMethod("CASH");
        tenancy.setDepositAmount("300.00");
        tenancy.setTenancyDepositSchemeAdministrator("Mr Administrator man");
        tenancy.setTenancyDepositSchemeContactDetails("contanct details for administrator");
        return tenancy;
    }

    public ModelTenancy tenancyWithGuarentors() {
        ModelTenancy modelTenancy = anyTenancy();
        List<Guarantor> guarantors = modelTenancy.getTenants().stream().map(tenant -> {
            Guarantor guarantor = new Guarantor();
            guarantor.setName("guarentor name for " + tenant.getName());
            guarantor.setAddress("guarentor address for " + tenant.getName());
            guarantor.setTenantNames(Collections.singletonList(tenant.getName()));
            return guarantor;
        }).collect(Collectors.toList());
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
        tenancy.setLandlords(Collections.emptyList());
        return tenancy;
    }

    public ModelTenancy tenancyWithOneLandlord() {
        return anyTenancy();
    }

    public ModelTenancy tenancyWithTwoLandlords() {
        ModelTenancy tenancy = anyTenancy();
        List<AgentOrLandLord> landlords = new ArrayList<>();
        Collections.addAll(landlords, anyAgentOrLandlord(), anyAgentOrLandlord());
        tenancy.setLandlords(landlords);
        return tenancy;
    }

    public ModelTenancy tenancyWithThreeLandlords() {
        ModelTenancy tenancy = anyTenancy();
        List<AgentOrLandLord> landlords = new ArrayList<>();
        Collections.addAll(landlords, anyAgentOrLandlord(), anyAgentOrLandlord(), anyAgentOrLandlord());
        tenancy.setLandlords(landlords);
        return tenancy;
    }

    public AgentOrLandLord anyAgentOrLandlord() {
        AgentOrLandLord agentOrLandLord = new AgentOrLandLord();
        agentOrLandLord.setName("name");
        agentOrLandLord.setEmail("ddd@ddd.com");
        agentOrLandLord.setAddress("address");
        agentOrLandLord.setTelephone("111");
        agentOrLandLord.setRegistrationNumber(validRegNumbers().get(0));
        return agentOrLandLord;
    }

    public List<Person> validPeople() {
        List<Person> people = new ArrayList<>();
        Collections.addAll(people,
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
        Collections.addAll(people,
                emptyPerson(),
                personWithNoContactInfo(),
                personWithInvalidEmail()
        );
        return people;
    }

    public Person validPerson() {
        return personWithAllFields();
    }

    public Person personWithAllFields() {
        Person person = new Person();
        person.setName("name");
        person.setEmail("ddd@ddd.com");
        person.setAddress("address");
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
        Collections.addAll(emails, "asd", "", null, "@gmail.com", "google@ggogle@google", "http://www.google.com/");
        return emails;
    }

    public List<String> validEmails() {
        List<String> emails = new ArrayList<>();
        Collections.addAll(emails, "david.sinclait@gov.scot", "someone@blah.blah.blah");
        return emails;
    }

    public List<String> invalidRegNumbers() {
        List<String> registrationNumbers = new ArrayList<>();
        Collections.addAll(registrationNumbers, null, "", "aaa", "1111111/222/1111111", "11111111111111111111");
        return registrationNumbers;
    }

    public List<String> validRegNumbers() {
        List<String> registrationNumbers = new ArrayList<>();
        Collections.addAll(registrationNumbers, "666666/333/55555");
        return registrationNumbers;
    }

}
