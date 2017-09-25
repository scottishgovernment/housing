package scot.mygov.housing;

import dagger.ObjectGraph;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import scot.mygov.housing.mapcloud.Heartbeat;
import scot.mygov.housing.mapcloud.Mapcloud;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Housing {

    private static final Logger LOG = LoggerFactory.getLogger(Housing.class);

    @Inject
    HousingConfiguration config;

    @Inject
    Mapcloud mapcloud;

    @Inject
    HousingApplication app;

    public static final void main(String[] args) throws Exception{
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        ObjectGraph graph = ObjectGraph.create(new HousingModule());

        // start the app
        graph.get(Housing.class).run();
    }

    public void run() {
        Server server = new Server();
        server.deploy(app);
        server.start(Undertow.builder().addHttpListener(config.getPort(), "::"));
        LOG.info("Listening on port {}", server.port());

        // schedule the mapcloud heartbeat
        Heartbeat heartbeat = new Heartbeat(mapcloud);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(heartbeat, 1, config.getMapcloudMonitoringInterval(), TimeUnit.MINUTES);
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

}
