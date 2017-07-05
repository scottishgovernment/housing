package scot.mygov.housing.modeltenancy.model;

import java.util.ArrayList;
import java.util.List;

public class Guarantor {

    private String name;
    private Address address;
    private List<String> tenantNames = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getTenantNames() {
        return tenantNames;
    }

    public void setTenantNames(List<String> tenantNames) {
        this.tenantNames = tenantNames;
    }
}
