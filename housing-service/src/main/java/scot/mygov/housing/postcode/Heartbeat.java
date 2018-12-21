package scot.mygov.housing.postcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.europa.EuropaException;

/**
 * Periodically perform a postcode lookup.
 *
 * This is used to ensure that metrics are available to the healthcheck while not calling it too frequently.
 */
public class Heartbeat implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Heartbeat.class);

    private static final String KNOWN_UPRN = "906030092";

    private final Europa europa;

    public Heartbeat(Europa europa) {
        this.europa = europa;
    }

    @Override
    public void run() {
        try {
            europa.lookupUprn(KNOWN_UPRN);
        } catch (EuropaException e) {
            LOG.error("Failed to lookup know uprn " + KNOWN_UPRN, e);
        }
    }
}
