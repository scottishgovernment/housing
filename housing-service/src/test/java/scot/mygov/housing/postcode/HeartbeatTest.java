package scot.mygov.housing.postcode;

import org.junit.Test;
import org.mockito.Mockito;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.europa.EuropaException;

import static org.mockito.ArgumentMatchers.any;

public class HeartbeatTest {
//    @Override
//    public void run() {
//        try {
//            europa.lookupUprn(KNOWN_UPRN);
//        } catch (EuropaException e) {
//            LOG.error("Failed to lookup know uprn " + KNOWN_UPRN, e);
//        }
//    }

    @Test
    public void greenpath() {
        Europa europa = Mockito.mock(Europa.class);
        Heartbeat sut = new Heartbeat(europa);
        sut.run();
    }

    @Test
    public void europaExceptionIsCaught() throws EuropaException {
        Europa europa = Mockito.mock(Europa.class);
        Mockito.when(europa.lookupUprn(any())).thenThrow(new EuropaException("", null));
        Heartbeat sut = new Heartbeat(europa);
        sut.run();
    }
}
