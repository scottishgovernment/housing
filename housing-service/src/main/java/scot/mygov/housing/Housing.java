package scot.mygov.housing;

import dagger.Component;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.postcode.Heartbeat;
import software.amazon.awssdk.services.s3.S3Client;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Housing {

    private static final Logger LOG = LoggerFactory.getLogger(Housing.class);

    @Inject
    public Housing() {
        // Default constructor
    }

    @Inject
    HousingConfiguration config;

    @Inject
    Europa europa;

    @Inject
    HousingApplication app;

    @Inject
    @Nullable
    S3Client s3;

    public static final void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        S3URLStreamHandlerFactory.register();
        Housing housing = DaggerHousing_Main.create().main();
        S3URLStreamHandlerFactory.setS3(housing.s3);

        // start the app
        housing.run();
    }

    public void run() {
        Server server = new Server();
        server.deploy(app);
        server.start(Undertow.builder().addHttpListener(config.getPort(), "::"));
        LOG.info("Listening on port {}", server.port());

        // schedule the postcode lookup heartbeat
        Heartbeat heartbeat = new Heartbeat(europa);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(heartbeat, 1, config.getHeartbeatMonitoringInterval(), TimeUnit.MINUTES);
    }

    public static class Server extends UndertowJaxrsServer {
        public int port() {
            InetSocketAddress address = (InetSocketAddress) server
                    .getListenerInfo()
                    .get(0)
                    .getAddress();
            return address.getPort();
        }
    }

    @Singleton
    @Component(modules = HousingModule.class)
    interface Main {
        Housing main();
    }

}
