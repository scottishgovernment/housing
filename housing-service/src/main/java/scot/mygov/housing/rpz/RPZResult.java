package scot.mygov.housing.rpz;

public class RPZResult {

    private boolean inRentPressureZone;
    private String rentPressureZoneTitle;
    private String dateFrom;
    private String dateTo;
    private double maxIncrease;

    private RPZResult(
            boolean inRentPressureZone,
            String rentPressureZoneTitle,
            String dateFrom,
            String dateTo,
            double maxIncrease) {
        this.inRentPressureZone = inRentPressureZone;
        this.rentPressureZoneTitle = rentPressureZoneTitle;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.maxIncrease = maxIncrease;
    }

    public boolean isInRentPressureZone() {
        return inRentPressureZone;
    }

    public String getRentPressureZoneTitle() {
        return rentPressureZoneTitle;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public double getMaxIncrease() {
        return maxIncrease;
    }

    public static class Builder {
        private boolean inRentPressureZone;
        private String rentPressureZoneTitle;
        private String dateFrom;
        private String dateTo;
        private double maxIncrease;

        public Builder inRentPressureZone(boolean inRentPressureZone) {
            this.inRentPressureZone = inRentPressureZone;
            return this;
        }

        public Builder title(String rentPressureZoneTitle) {
            this.rentPressureZoneTitle = rentPressureZoneTitle;
            return this;
        }

        public Builder dateFrom(String dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public Builder dateTo(String dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public Builder maxIncrease(double maxIncrease) {
            this.maxIncrease = maxIncrease;
            return this;
        }


        public RPZResult build() {
            return new RPZResult(inRentPressureZone, rentPressureZoneTitle, dateFrom, dateTo, maxIncrease);
        }
    }



}
