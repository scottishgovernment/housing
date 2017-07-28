package scot.mygov.housing.cpi;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CPIServiceTest {

    @Test
    public void calculatesExpectedDeltaForKnownDates() throws IOException, CPIServiceException {
        // ARRANGE
        CPIService sut = new CPIService(sampleDataUrl());
        LocalDate from = LocalDate.of(2017, 4, 1);
        LocalDate to = LocalDate.of(2017, 5, 1);
        double expected = 0.4;

        // ACT
        double actual = sut.cpiDelta(from, to);

        // ASSERT
        assertEquals("unexpected delta", expected, actual, 0.0001);
    }

    @Test
    public void deltaIsZeroForSameFromAndToDates() throws IOException, CPIServiceException {
        // ARRANGE
        CPIService sut = new CPIService(sampleDataUrl());
        LocalDate from = LocalDate.of(2017, 4, 1);
        LocalDate to = LocalDate.of(2017, 4, 1);
        double expected = 0.0;

        // ACT
        double actual = sut.cpiDelta(from, to);

        // ASSERT
        assertEquals("unexpected delta", expected, actual, 0.0);
    }

    @Test
    public void usesFirstAvailableFigureForFarPastFromDate() throws IOException, CPIServiceException {
        // ARRANGE
        CPIService sut = new CPIService(sampleDataUrl());
        LocalDate from = LocalDate.MIN;
        LocalDate to = LocalDate.of(2017, 4, 1);
        double expected = 54.5;

        // ACT
        double actual = sut.cpiDelta(from, to);

        // ASSERT
        assertEquals("unexpected delta", expected, actual, 0.0001);
    }

    @Test
    public void usesFirstAvailableFigureForFarFutureToDate() throws IOException, CPIServiceException {
        // ARRANGE
        CPIService sut = new CPIService(sampleDataUrl());
        LocalDate from = LocalDate.of(2017, 4, 1);
        LocalDate to = LocalDate.MAX;
        double expected = 0.4;

        // ACT
        double actual = sut.cpiDelta(from, to);

        // ASSERT
        assertEquals("unexpected delta", expected, actual, 0.0001);
    }

    @Test(expected = CPIServiceException.class)
    public void dataNotFoundWrappedInServiceException() throws IOException, CPIServiceException {
        // ARRANGE
        CPIService sut = new CPIService(new URL("http://localhost/nosuchfile"));

        // ACT
        double actual = sut.cpiDelta(LocalDate.now(), LocalDate.now());

        // ASSERT -- see expected exception
    }

    public URL sampleDataUrl() throws IOException {
        URLConnection mockConnection = mock(URLConnection.class);
        InputStream in = CPIServiceTest.class.getResourceAsStream("sampleData.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        URLStreamHandler handler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(final URL arg0)
                    throws IOException {
                return mockConnection;
            }
        };
        return new URL("", "", 80, "", handler);
    }
}
