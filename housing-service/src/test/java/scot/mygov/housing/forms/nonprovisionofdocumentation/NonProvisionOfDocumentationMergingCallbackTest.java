package scot.mygov.housing.forms.nonprovisionofdocumentation;

import com.aspose.words.Document;
import com.aspose.words.FieldMergingArgs;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NonProvisionOfDocumentationMergingCallbackTest {

    private NonProvisionOfDocumentationMergingCallback sut = new NonProvisionOfDocumentationMergingCallback();

    @Test
    public void placeholderFieldWithValue() throws Exception {
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("value");
        when(args.getFieldName()).thenReturn("nameOfLandlordOrAgent");
        sut.fieldMerging(args);
    }

    @Test
    public void placeholderFieldWithEmptyValue() throws Exception {
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("nameOfLandlordOrAgent");
        when(args.getDocument()).thenReturn(new Document());
        sut.fieldMerging(args);
    }

    @Test
    public void placeholderFieldWithNullValue() throws Exception {
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn(null);
        when(args.getFieldName()).thenReturn("nameOfLandlordOrAgent");
        when(args.getDocument()).thenReturn(new Document());
        sut.fieldMerging(args);
    }

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
}
