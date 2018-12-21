package scot.mygov.housing.europa;

import java.util.ArrayList;
import java.util.List;

public class AddressResultWrapper {

    private List<EuropaAddress> address = new ArrayList<>();

    public List<EuropaAddress> getAddress() {
        return address;
    }

    public void setAddress(List<EuropaAddress> address) {
        this.address = address;
    }
}
