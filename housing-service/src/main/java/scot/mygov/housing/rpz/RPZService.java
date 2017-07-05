package scot.mygov.housing.rpz;

import java.time.LocalDate;

public interface RPZService {

    RPZResult rpz(String postcode, LocalDate date);
}
