package scot.mygov.documents;

import com.aspose.words.Document;
import org.junit.Test;
import org.mockito.Mockito;


import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Created by z418868 on 25/09/2020.
 */
public class DateSwitchingDocumentTemplateLoaderTest {

    @Test
    public void usesOldTemplateForDateBeforeCovidDate() {
        DateSwitchingDocumentTemplateLoader sut = new DateSwitchingDocumentTemplateLoader();
        DocumentTemplateLoader oldLoader = Mockito.mock(DocumentTemplateLoader.class);
        DocumentTemplateLoader newLoader = Mockito.mock(DocumentTemplateLoader.class);
        sut.addDocument(LocalDate.of(2009, 01, 01), oldLoader);
        sut.addDocument(LocalDate.of(2010, 01, 01), newLoader);
        sut.localDateSuplier = () -> LocalDate.of(2009, 10, 01);

        // ACT
        sut.loadDocumentTemplate();

        // ASSERT
        Mockito.verify(newLoader, never()).loadDocumentTemplate();
        Mockito.verify(oldLoader, times(1)).loadDocumentTemplate();
    }

    @Test
    public void usesNewTemplateForDateOnCovidDate() {
        DateSwitchingDocumentTemplateLoader sut = new DateSwitchingDocumentTemplateLoader();
        DocumentTemplateLoader oldLoader = Mockito.mock(DocumentTemplateLoader.class);
        DocumentTemplateLoader newLoader = Mockito.mock(DocumentTemplateLoader.class);
        sut.addDocument(LocalDate.of(2009, 01, 01), oldLoader);
        sut.addDocument(LocalDate.of(2010, 01, 01), newLoader);
        sut.localDateSuplier = () -> LocalDate.of(2010, 01, 01);

        // ACT
        sut.loadDocumentTemplate();

        // ASSERT
        Mockito.verify(oldLoader, never()).loadDocumentTemplate();
        Mockito.verify(newLoader, times(1)).loadDocumentTemplate();
    }

    @Test
    public void usesRighttmeplateForDateAfterCovidDate() {
        DateSwitchingDocumentTemplateLoader sut = new DateSwitchingDocumentTemplateLoader();
        DocumentTemplateLoader oldLoader = Mockito.mock(DocumentTemplateLoader.class);
        DocumentTemplateLoader newLoader = Mockito.mock(DocumentTemplateLoader.class);
        sut.addDocument(LocalDate.of(2009, 01, 01), oldLoader);
        sut.addDocument(LocalDate.of(2010, 01, 01), newLoader);
        sut.localDateSuplier = () -> LocalDate.of(2020, 01, 01);

        // ACT
        sut.loadDocumentTemplate();

        // ASSERT
        Mockito.verify(oldLoader, never()).loadDocumentTemplate();
        Mockito.verify(newLoader, times(1)).loadDocumentTemplate();
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
