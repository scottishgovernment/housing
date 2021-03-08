package scot.mygov.housing.forms.foreigntraveldeclaration.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by z418868 on 08/03/2021.
 */
public enum TravelReason {

    WORK("work"),
    VOLUNTEERING("volunteering"),
    EDUCATION("education"),
    MEDICAL_OR_COMPASSIONATE("medical-or-compassionate"),
    WEDDING_FUNERAL("WEDDING_FUNERAL"),
    OTHER("other");

    private static final Logger LOG = LoggerFactory.getLogger(scot.mygov.housing.forms.foreigntraveldeclaration.model.TravelReason.class);

    private final String description;

    TravelReason(String description) {
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
            TravelReason reason = TravelReason.valueOf(type);
            return reason.getDescription();
        } catch (IllegalArgumentException e) {
            LOG.warn("Unexpected travel reason: " + type, e);
            return type;
        }
    }
}