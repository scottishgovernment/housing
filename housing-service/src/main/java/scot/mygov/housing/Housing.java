package scot.mygov.housing;

import dagger.ObjectGraph;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.inject.Inject;
import java.net.InetSocketAddress;

public class Housing {

    private static final Logger LOG = LoggerFactory.getLogger(Housing.class);

    @Inject
    HousingConfiguration config;

    @Inject
    HousingApplication app;

    public static final void main(String[] args) throws Exception{
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        ObjectGraph graph = ObjectGraph.create(new HousingModule());
        graph.get(Housing.class).run();
    }

    public void run() {
        Server server = new Server();
        server.deploy(app);
        server.start(Undertow.builder().addHttpListener(config.getPort(), "::"));
        LOG.info("Listening on port {}", server.port());
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
