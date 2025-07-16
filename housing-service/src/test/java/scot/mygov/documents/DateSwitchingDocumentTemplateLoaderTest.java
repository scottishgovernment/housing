package scot.mygov.documents;

import com.aspose.words.Document;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DateSwitchingDocumentTemplateLoaderTest {

    @Test
    public void usesOldTemplateForDateBeforeCovidDate() {
        DateSwitchingDocumentTemplateLoader sut = new DateSwitchingDocumentTemplateLoader();
        DocumentTemplateLoader oldLoader = mock(DocumentTemplateLoader.class);
        DocumentTemplateLoader newLoader = mock(DocumentTemplateLoader.class);
        sut.addDocument(LocalDate.of(2009, 01, 01), oldLoader);
        sut.addDocument(LocalDate.of(2010, 01, 01), newLoader);
        sut.localDateSupplier = () -> LocalDate.of(2009, 10, 01);

        // ACT
        sut.loadDocumentTemplate();

        // ASSERT
        verify(newLoader, never()).loadDocumentTemplate();
        verify(oldLoader, times(1)).loadDocumentTemplate();
    }

    @Test
    public void usesNewTemplateForDateOnCovidDate() {
        DateSwitchingDocumentTemplateLoader sut = new DateSwitchingDocumentTemplateLoader();
        DocumentTemplateLoader oldLoader = mock(DocumentTemplateLoader.class);
        DocumentTemplateLoader newLoader = mock(DocumentTemplateLoader.class);
        sut.addDocument(LocalDate.of(2009, 01, 01), oldLoader);
        sut.addDocument(LocalDate.of(2010, 01, 01), newLoader);
        sut.localDateSupplier = () -> LocalDate.of(2010, 01, 01);

        // ACT
        sut.loadDocumentTemplate();

        // ASSERT
        verify(oldLoader, never()).loadDocumentTemplate();
        verify(newLoader, times(1)).loadDocumentTemplate();
    }

    @Test
    public void usesRightTemplateForDateAfterCovidDate() {
        DateSwitchingDocumentTemplateLoader sut = new DateSwitchingDocumentTemplateLoader();
        DocumentTemplateLoader oldLoader = mock(DocumentTemplateLoader.class);
        DocumentTemplateLoader newLoader = mock(DocumentTemplateLoader.class);
        sut.addDocument(LocalDate.of(2009, 01, 01), oldLoader);
        sut.addDocument(LocalDate.of(2010, 01, 01), newLoader);
        sut.localDateSupplier = () -> LocalDate.of(2020, 01, 02);

        // ACT
        sut.loadDocumentTemplate();

        // ASSERT
        verify(oldLoader, never()).loadDocumentTemplate();
        verify(newLoader, times(1)).loadDocumentTemplate();
    }

    @Test
    public void returnsNullIfNoMatchingDate() {
        DateSwitchingDocumentTemplateLoader sut = new DateSwitchingDocumentTemplateLoader();

        // ACT
        Document actual = sut.loadDocumentTemplate();

        // ASSERT
        assertNull(actual);
    }
}
