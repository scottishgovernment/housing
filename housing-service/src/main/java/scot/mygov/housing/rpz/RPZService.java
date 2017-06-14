package scot.mygov.housing.rpz;

import java.time.LocalDate;

/**
 * Created by z418868 on 14/06/2017.
 */
public interface RPZService {

    RPZResult rpz(String postcode, LocalDate date);
}
