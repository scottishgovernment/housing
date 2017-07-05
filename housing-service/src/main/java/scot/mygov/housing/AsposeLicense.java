package scot.mygov.housing;

import com.aspose.words.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.*;

public class AsposeLicense {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsposeLicense.class);

    private License license = new License();

    private LocalDate expiryDate;

    public AsposeLicense(File licenseFile) {
        if (licenseFile == null) {
            LOGGER.warn("No Aspose Words license file configured");
            return;
        }
        try {
            loadLicense(licenseFile.toURI());
        } catch (Exception ex) {
            LOGGER.warn("Failed to load aspose license from " + licenseFile, ex);
        }
    }

    private void loadLicense(URI licenseURI) throws Exception {
        if (!new File(licenseURI).exists()) {
            LOGGER.warn("License file does not exist: {}", licenseURI);
            return;
        }
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        InputSource source = new InputSource(licenseURI.toString());
        String dateString = xPath.evaluate("//License/Data/SubscriptionExpiry", source);
        expiryDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
        license.setLicense(licenseURI.getPath());
    }

    public LocalDate expires() {
        return expiryDate;
    }

    public Long daysUntilExpiry() {
        if (expiryDate == null) {
            return null;
        }
        return DAYS.between(LocalDate.now(), expiryDate);
    }

    public boolean isLicensed() {
        return license.getIsLicensed();
    }

}

