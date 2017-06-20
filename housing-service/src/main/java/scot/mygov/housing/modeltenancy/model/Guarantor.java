package scot.mygov.housing.modeltenancy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z418868 on 20/06/2017.
 */
public class Guarantor {

    private String name;
    private String address;
    private List<String> tenantNames = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getTenantNames() {
        return tenantNames;
    }

    public void setTenantNames(List<String> tenantNames) {
        this.tenantNames = tenantNames;
    }
}
