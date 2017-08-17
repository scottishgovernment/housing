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

        public Builder rpz(RPZ rpz) {
            this.inRentPressureZone = true;
            this.rentPressureZoneTitle = rpz.getTitle();
            this.maxIncrease = rpz.getMaxRentIncrease();
            return this;
        }

        public RPZResult build() {
            return new RPZResult(inRentPressureZone, rentPressureZoneTitle, maxIncrease);
        }
    }



}
