package scot.mygov.housing.cpi;

import com.fasterxml.jackson.databind.ObjectMapper;
import scot.mygov.housing.cpi.model.CPIData;
import scot.mygov.housing.cpi.model.CPIDataPoint;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.SortedMap;
import java.util.TreeMap;

public class CPIService {

    private final URL dataURL;

    public CPIService(URL dataURI) {
        this.dataURL = dataURI;
    }

    public double cpiDelta(LocalDate fromDate, LocalDate toDate) throws CPIServiceException {
        SortedMap<YearMonth, Double> cpiFigures = cpiFigures();
        double fromCPI = cpi(fromDate, cpiFigures);
        double toCPI = cpi(toDate, cpiFigures);
        double delta =  toCPI - fromCPI;
        return BigDecimal.valueOf(delta).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public CPIData cpiData() throws CPIServiceException {
        try{
            return new ObjectMapper().readValue(dataURL, CPIData.class);
        } catch (IOException e) {
            throw new CPIServiceException("Unable to fetch CPI data", e);
        }
    }

    private SortedMap<YearMonth, Double> cpiFigures() throws CPIServiceException{
        CPIData cpiData = cpiData();
        SortedMap<YearMonth, Double> cpiFigures = new TreeMap<>();
        for (CPIDataPoint dataPoint : cpiData.getData()) {
            YearMonth yearMonth = YearMonth.of(dataPoint.getYear(), dataPoint.getMonth());
            cpiFigures.put(yearMonth, dataPoint.getValue());
        }
        return cpiFigures;
    }

    private double cpi(LocalDate date, SortedMap<YearMonth, Double> cpiFigures) {
        YearMonth yearMonth = YearMonth.from(date);

        // if this date is before the oldest month we have data for, then use the first date.
        if (cpiFigures.firstKey().isAfter(yearMonth)) {
            return cpiFigures.get(cpiFigures.firstKey());
        }

        // if this date is after the most recent month we have data for then use the most recent figure
        if (cpiFigures.lastKey().isBefore(yearMonth)) {
            return cpiFigures.get(cpiFigures.lastKey());
        }

        return cpiFigures.get(yearMonth);
    }
}
