package scot.mygov.housing.rpz;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by z418868 on 14/06/2017.
 */
public class RPZ {
    private String title;
    private LocalDate from;
    private LocalDate to;
    private double maxRentIncrease;
    private Set<String> postcodes;

    public RPZ(String title, LocalDate from, LocalDate to, double maxRentIncrease, Set<String> postocdes) {
        this.title = title;
        this.from = from;
        this.to = to;
        this.maxRentIncrease = maxRentIncrease;
        this.postcodes = postocdes;
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
}
