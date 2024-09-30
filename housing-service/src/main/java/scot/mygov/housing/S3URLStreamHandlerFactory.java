package scot.mygov.housing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class S3URLStreamHandlerFactory implements URLStreamHandlerFactory {

    static final S3URLStreamHandlerFactory INSTANCE =
            new S3URLStreamHandlerFactory();

    private static S3Client s3Client;

    private S3URLStreamHandlerFactory() {
        // Singleton instance
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (!"s3".equals(protocol)) {
            return null;
        }
        return new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL url) throws IOException {
                return new S3URLConnection(url);
            }
        };
    }

    public static void register() {
        URL.setURLStreamHandlerFactory(INSTANCE);
    }

    public static void setS3(S3Client s3) {
        s3Client = s3;
    }

    static class S3URLConnection extends URLConnection {

        private static final Logger LOG = LoggerFactory.getLogger(S3URLConnection.class);

        public S3URLConnection(URL url) {
            super(url);
        }

        public void connect() throws IOException {
            if (S3URLStreamHandlerFactory.s3Client == null) {
                throw new IOException("S3 client not initialised");
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (S3URLStreamHandlerFactory.s3Client == null) {
                throw new IOException("S3 client not initialised");
            }
            String bucket = this.getURL().getHost();
            // Strip first character of path, which should be a slash
            String key = this.getURL().getPath().substring(1);
            LOG.info("Fetching s3://{}/{}", bucket, key);
            try {
                GetObjectRequest req = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                return S3URLStreamHandlerFactory.s3Client.getObject(req);
            } catch (S3Exception ex) {
                String url = "s3://" + bucket + "/" + key;
                throw new IOException("Could not fetch from s3: " + url, ex);
            }
        }

    }

}
