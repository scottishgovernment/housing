package scot.mygov.housing;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import scot.mygov.housing.S3URLStreamHandlerFactory.S3URLConnection;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static org.assertj.core.api.Assertions.assertThat;

public class S3URLStreamHandlerFactoryTest {

    private static URL testS3Url;

    private S3URLStreamHandlerFactory factory = S3URLStreamHandlerFactory.INSTANCE;

    @BeforeClass
    public static void setUp() throws IOException {
        S3URLStreamHandlerFactory.register();
        testS3Url = new URL("s3://mybucket/prefix/name");
    }

    @Test
    public void returnsS3HandlerForS3URL() throws IOException {
        URLStreamHandler handler = factory.createURLStreamHandler("s3");
        assertThat(handler).isNotNull();
        URLConnection conn = new URL(testS3Url, "key", handler).openConnection();
        assertThat(conn).isInstanceOf(S3URLConnection.class);
    }

    @Test
    public void returnsNullForNonS3URL() throws IOException {
        URLStreamHandler handler = factory.createURLStreamHandler("http");
        assertThat(handler).isNull();
    }

    @Test(expected=IOException.class)
    public void throwsIOExceptionIfConnectBeforeInitialisation() throws IOException {
        S3URLStreamHandlerFactory.setS3(null);
        new S3URLConnection(testS3Url).connect();
    }

    @Test(expected=IOException.class)
    public void throwsIOExceptionIfOpenBeforeInitialisation() throws IOException {
        S3URLStreamHandlerFactory.setS3(null);
        new S3URLConnection(testS3Url).getInputStream();
    }

    @Test
    public void ignoresConnectCallIfInitialised() throws IOException {
        S3URLStreamHandlerFactory.setS3(AmazonS3ClientBuilder.standard().withRegion("eu-west-1").build());
        new S3URLConnection(testS3Url).connect();
    }

}
