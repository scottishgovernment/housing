package scot.mygov.housing.forms.rentadjudication;

import scot.mygov.housing.forms.modeltenancy.model.AgentOrLandLord;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyObjectMother;
import scot.mygov.housing.forms.rentadjudication.model.RentAdjudication;
import scot.mygov.housing.forms.rentadjudication.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RentAdjudicationObjectMother {

    public static RentAdjudication anyRentAdjudication() {
        RentAdjudication rentAdjudication = new RentAdjudication();
        rentAdjudication.setTenants(somePeople("tenant", 3));
        rentAdjudication.setTenantAgent(person("tenantAgent"));
        rentAdjudication.setLandlords(someLandlords("landlord", 2));
        rentAdjudication.setLettingAgent(person("landlordAgent"));
        rentAdjudication.setPropertyType("Flat");
        rentAdjudication.setRooms(someRooms());
        rentAdjudication.setSharedAreas("sharedAreas");
        rentAdjudication.setIncluded("included");
        rentAdjudication.setHeating("heating");
        rentAdjudication.setDoubleGlazing("doubleGlazing");
        rentAdjudication.setServicesDetails("serviceDetails");
        rentAdjudication.setServicesCostDetails("serviceCostDetails");
        rentAdjudication.setFurnished("furnished");
        rentAdjudication.setImprovementsTenant("tenantImprovements");
        rentAdjudication.setImprovementsLandlord("landlordImprovements");
        rentAdjudication.setDamage("damage");
        rentAdjudication.setCurrentRentAmount("100");
        rentAdjudication.setCurrentRentFrequency("WEEKLY");
        rentAdjudication.setNewRentAmount("100");
        rentAdjudication.setNewRentFrequency("CALENDAR_MONTH");
        return rentAdjudication;
    }

    public static List<Person> somePeople(String prefix, int count) {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            people.add(person(prefix + i));
        }
        return people;
    }

    public static List<AgentOrLandLord> someLandlords(String prefix, int count) {
        List<AgentOrLandLord> people = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            people.add(landlord(prefix + i));
        }
        return people;
    }

    public static Person person(String prefix) {
        Person p = new Person();
        p.setName(prefix + "anyTenant");
        p.setAddress(new ModelTenancyObjectMother().validAddress(prefix));
        p.setEmail(prefix + "@ddd.com");
        return p;
    }

    public static AgentOrLandLord landlord(String prefix) {
        AgentOrLandLord p = new AgentOrLandLord();
        p.setName(prefix + "anyTenant");
        p.setAddress(new ModelTenancyObjectMother().validAddress(prefix));
        p.setEmail(prefix + "@ddd.com");
        p.setRegistrationNumber("111111/111/11111");
        return p;
    }

    public static List<Room> someRooms() {
        List<Room> rooms = new ArrayList<>();
        Collections.addAll(rooms,
                room("Bedroom", "3"),
                room("Kitchen", "1"),
                room("Bathroom", "2"));
        return rooms;
    }

    public static Room room(String name, String quantity) {
        Room room = new Room();
        room.setName(name);
        room.setQuantity(quantity);
        return room;
    }
}
