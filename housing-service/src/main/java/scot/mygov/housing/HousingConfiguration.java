package scot.mygov.housing;

import java.net.URI;

public class HousingConfiguration {

    private int port = 8082;

    private URI index = URI.create("http://localhost:9200/livecontent");

    public int getPort() {
        return port;
    }

    public URI getIndex() {
        return index;
    }

}
