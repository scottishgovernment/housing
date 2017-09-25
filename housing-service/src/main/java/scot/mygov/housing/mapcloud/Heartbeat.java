package scot.mygov.housing.mapcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Periodically perform mapcloud lookup.  This is used to ensure that metrics are available to the healthcheck
 * wqhile not calling it too frequently.
 */
public class Heartbeat implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Heartbeat.class);

    private static final String KNOWN_UPRN = "906030092";

    private final Mapcloud mapcloud;

    public Heartbeat(Mapcloud mapcloud) {
        this.mapcloud = mapcloud;
    }

    @Override
    public void run() {
        try {
            mapcloud.lookupUprn(KNOWN_UPRN);
        } catch (MapcloudException e) {
            LOG.error("Failed to lookup know uprn " + KNOWN_UPRN, e);
        }
    }
}
