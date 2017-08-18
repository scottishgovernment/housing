package scot.mygov.housing.modeltenancy;

import com.aspose.words.Field;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.FieldStart;
import com.aspose.words.Node;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmptySectionRemovingCallbackTest {

    @Test
    public void removesSectionWithEmptyField() throws Exception {

        // ARRANGE
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("field");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Node.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(ancestor).remove();
    }

    @Test
    public void doesNotRemoveSectionWithNonEmptyField() throws Exception {

        // ARRANGE
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("value");
        when(args.getFieldName()).thenReturn("field");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Node.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(ancestor, Mockito.never()).remove();
    }

    @Test
    public void ignoresEmptyFieldNotInFieldSet() throws Exception {

        // ARRANGE
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("anotherfield");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Node.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(ancestor, Mockito.never()).remove();
    }


    @Test
    public void imageFieldMergingDoesNothing() throws Exception {
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        sut.imageFieldMerging(null);
    }
    // removes null fields

    // leaves other feidls


}
