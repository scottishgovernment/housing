package scot.mygov.housing.rpz;

import java.time.LocalDate;
import java.util.Set;

public class RPZ {
    private String title;
    private LocalDate from;
    private LocalDate to;
    private double maxRentIncrease;
    private Set<String> postcodes;
    private Set<String> uprns;
    private String username;
    private LocalDate updatedDate;

    public RPZ(
            String title,
            LocalDate from,
            LocalDate to,
            double maxRentIncrease,
            Set<String> postcodes,
            Set<String> uprns,
            String username,
            LocalDate updatedDate) {
        this.title = title;
        this.from = from;
        this.to = to;
        this.maxRentIncrease = maxRentIncrease;
        this.postcodes = postcodes;
        this.uprns = uprns;
        this.username = username;
        this.updatedDate = updatedDate;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public double getMaxRentIncrease() {
        return maxRentIncrease;
    }

    public Set<String> getPostcodes() {
        return postcodes;
    }

    public Set<String> getUprns() {
        return uprns;
    }

    public String getUsername() { return username; }

    public LocalDate getUpdatedDate() { return updatedDate; }
}
