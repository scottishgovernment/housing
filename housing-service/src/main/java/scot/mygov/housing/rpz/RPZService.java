package scot.mygov.housing.rpz;

import java.time.LocalDate;

public interface RPZService {

    RPZResult rpz(String uprn, LocalDate date) throws RPZServiceException;
}
