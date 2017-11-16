package scot.mygov.housing.forms;

import com.aspose.words.Document;
import com.aspose.words.FieldMergingArgs;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static scot.mygov.documents.PlaceholderUtils.writeLines;

public class PlaceholderProvidingMergingCallbackTest {

    private PlaceholderProvidingMergingCallback sut
            = new PlaceholderProvidingMergingCallback(
                Collections.singletonMap("tenantNames", documentBuilder -> writeLines(documentBuilder, 5)));

    @Test
    public void nonPlaceholderFieldWithValue() throws Exception {
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("value");
        when(args.getFieldName()).thenReturn("unknowValue");
        sut.fieldMerging(args);
    }

    @Test
    public void nonPlaceholderFieldWithEmptyValue() throws Exception {
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("unknowValue");
        sut.fieldMerging(args);
    }

    @Test
    public void placeholderFieldWithEmptyValue() throws Exception {
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("tenantNames");
        when(args.getDocument()).thenReturn(new Document());
        sut.fieldMerging(args);
    }

    @Test
    public void placeholderFieldWithNonEmptyValue() throws Exception {
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("names");
        when(args.getFieldName()).thenReturn("tenantNames");
        sut.fieldMerging(args);
    }
}
