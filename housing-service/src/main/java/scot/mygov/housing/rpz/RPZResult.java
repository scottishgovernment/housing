package scot.mygov.housing.rpz;

public class RPZResult {

    private boolean inRentPressureZone;
    private String rentPressureZoneTitle;
    private double maxIncrease;

    private RPZResult(
            boolean inRentPressureZone,
            String rentPressureZoneTitle,
            double maxIncrease) {
        this.inRentPressureZone = inRentPressureZone;
        this.rentPressureZoneTitle = rentPressureZoneTitle;
        this.maxIncrease = maxIncrease;
    }

    public boolean isInRentPressureZone() {
        return inRentPressureZone;
    }

    public String getRentPressureZoneTitle() {
        return rentPressureZoneTitle;
    }

    public double getMaxIncrease() {
        return maxIncrease;
    }

    public static class Builder {
        private boolean inRentPressureZone;
        private String rentPressureZoneTitle;
        private double maxIncrease;

        public Builder inRentPressureZone(boolean inRentPressureZone) {
            this.inRentPressureZone = inRentPressureZone;
            return this;
        }

        public Builder title(String rentPressureZoneTitle) {
            this.rentPressureZoneTitle = rentPressureZoneTitle;
            return this;
        }

        public Builder maxIncrease(double maxIncrease) {
            this.maxIncrease = maxIncrease;
            return this;
        }


        public RPZResult build() {
            return new RPZResult(inRentPressureZone, rentPressureZoneTitle, maxIncrease);
        }
    }



}
