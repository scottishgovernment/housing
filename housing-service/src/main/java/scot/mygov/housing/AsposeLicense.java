package scot.mygov.housing;

import com.aspose.words.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AsposeLicense {

    private static final Logger LOG = LoggerFactory.getLogger(AsposeLicense.class);

    private License license = new License();

    private LocalDate expiryDate;

    public AsposeLicense(URI licenseURI) {
        try {
            this.license = loadLicense(licenseURI);
        } catch (Exception e) {
            LOG.warn("Failed to load aspose license", e);
        }
    }

    private License loadLicense(URI licenseURI) throws Exception {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        InputSource source = new InputSource(licenseURI.toString());
        String dateString = xPath.evaluate("//License/Data/SubscriptionExpiry", source);
        expiryDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
        license.setLicense(licenseURI.getPath());
        return license;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isLicensed() {
        return license.getIsLicensed();
    }

    public static void main(String[] args) throws Exception {
        new AsposeLicense(URI.create("file:/Users/z418868/.config/Aspose.Words.lic"));
    }
}
