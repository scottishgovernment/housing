package scot.mygov.housing.forms.modeltenancy;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import scot.mygov.housing.AsposeLicense;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class AsposeLicenseTest {

    @Test
    public void isUnlicensedIfNoLicenseFileExists() throws URISyntaxException {
        AsposeLicense license = new AsposeLicense(null);
        assertFalse(license.isLicensed());
    }

    @Test
    public void daysRemainingIsOneIfLicenseExpiresTomorrow() throws URISyntaxException, IOException {
        File file = createLicenseWithExpiryDate("tomorrow", LocalDate.now().plusDays(1));
        AsposeLicense license = new AsposeLicense(file);
        assertEquals(Long.valueOf(1), license.daysUntilExpiry());
    }

    @Test
    public void daysRemainingIsZeroIfLicenseExpiresToday() throws URISyntaxException, IOException {
        File file = createLicenseWithExpiryDate("today", LocalDate.now());
        AsposeLicense license = new AsposeLicense(file);
        assertEquals(Long.valueOf(0), license.daysUntilExpiry());
    }

    @Test
    public void daysRemainingIsNegativeIfLicenseHasExpired() throws URISyntaxException {
        URI uri = getClass().getResource("/expired.lic").toURI();
        File file = new File(uri);
        AsposeLicense license = new AsposeLicense(file);
        assertTrue(license.daysUntilExpiry() < 0);
    }

    @Test
    public void daysRemainingIsNullIfUnlicensed() throws URISyntaxException {
        AsposeLicense license = new AsposeLicense(null);
        assertNull(license.daysUntilExpiry());
    }

    @Test
    public void expiryDateReturnsTheSubscriptionExpiryDate() throws URISyntaxException {
        URI uri = getClass().getResource("/license.lic").toURI();
        File file = new File(uri);
        AsposeLicense license = new AsposeLicense(file);
        LocalDate expected = LocalDate.of(2018, 05, 12);
        assertEquals(expected, license.expires());
    }

    @Test
    public void expiryDateIsNullIfUnlicensed() throws URISyntaxException {
        AsposeLicense license = new AsposeLicense(null);
        assertNull(license.expires());
    }

    private File createLicenseWithExpiryDate(String name, LocalDate date) {
        try {
            URI uri = getClass().getResource("/license.lic").toURI();
            File file = new File(new File(uri).getParentFile(), name + ".lic");
            String xml = IOUtils.toString(uri, Charset.forName("UTF-8"));
            String todayXml = xml.replaceAll("20180512", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            try (BufferedWriter writer = Files.newWriter(file, Charset.forName("UTF-8"))) {
                writer.write(todayXml);
            }
            return file;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
