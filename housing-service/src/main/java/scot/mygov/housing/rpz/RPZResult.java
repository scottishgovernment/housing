package scot.mygov.housing.rpz;

/**
 * Created by z418868 on 14/06/2017.
 */
public class RPZResult {

    private boolean validPostcode;
    private boolean scottishPostcode;
    private boolean inRentPressureZone;
    private String rentPressureZoneTitle;
    private double maxIncrease;

    public RPZResult(
            boolean validPostcode,
            boolean scottishPostcode,
            boolean inRentPressureZone,
            String rentPressureZoneTitle,
            double maxIncrease) {
        this.validPostcode = validPostcode;
        this.scottishPostcode = scottishPostcode;
        this.inRentPressureZone = inRentPressureZone;
        this.rentPressureZoneTitle = rentPressureZoneTitle;
        this.maxIncrease = maxIncrease;
    }

    public boolean isValidPostcode() {
        return validPostcode;
    }

    public boolean isScottishPostcode() {
        return scottishPostcode;
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
        private boolean validPostcode;
        private boolean scottishPostcode;
        private boolean inRentPressureZone;
        private String rentPressureZoneTitle;
        private double maxIncrease;

        public Builder validPostcode(boolean valid) {
            this.validPostcode = valid;
            return this;
        }

        public Builder scottishPostcode(boolean scottishPostcode) {
            this.scottishPostcode = scottishPostcode;
            return this;
        }

        public Builder inRentPressureZone(boolean inRentPressureZone) {
            this.inRentPressureZone = inRentPressureZone;
            return this;
        }

        public Builder rpz(RPZ rpz) {
            this.validPostcode = true;
            this.scottishPostcode = true;
            this.inRentPressureZone = true;
            this.rentPressureZoneTitle = rpz.getTitle();
            this.maxIncrease = rpz.getMaxRentIncrease();
            return this;
        }

        public RPZResult build() {
            return new RPZResult(validPostcode, scottishPostcode, inRentPressureZone, rentPressureZoneTitle, maxIncrease);
        }
    }



}
