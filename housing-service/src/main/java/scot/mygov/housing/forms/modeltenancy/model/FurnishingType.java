package scot.mygov.housing.forms.modeltenancy.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FurnishingType {
    UNFURNISHED("unfurnished"), FURNISHED("furnished"), PARTLY_FURNISHED("partly furnished");

    private static final Logger LOG = LoggerFactory.getLogger(FurnishingType.class);

    private final String description;

    FurnishingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String describe(String type) {

        if (StringUtils.isBlank(type)) {
            return "";
        }

        try {
            FurnishingType furnishingType = FurnishingType.valueOf(type);
            return furnishingType.getDescription();
        } catch (IllegalArgumentException e) {
            LOG.warn("Unexpected furnishing type: " + type, e);
            return type;
        }
    }
}
