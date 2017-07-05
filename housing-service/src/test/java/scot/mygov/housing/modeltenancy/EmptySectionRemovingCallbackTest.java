package scot.mygov.housing.modeltenancy;

import com.aspose.words.Field;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.FieldStart;
import com.aspose.words.Node;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Collections;
public class EmptySectionRemovingCallbackTest {

    @Test
    public void removesSectionWithEmptyField() throws Exception {

        // ARRANGE
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        FieldMergingArgs args = Mockito.mock(FieldMergingArgs.class);
        Mockito.when(args.getFieldValue()).thenReturn("");
        Mockito.when(args.getFieldName()).thenReturn("field");
        Field field = Mockito.mock(Field.class);
        FieldStart fieldStart = Mockito.mock(FieldStart.class);
        Node ancestor = Mockito.mock(Node.class);

        Mockito.when(args.getField()).thenReturn(field);
        Mockito.when(field.getStart()).thenReturn(fieldStart);
        Mockito.when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        Mockito.verify(ancestor).remove();
    }

    @Test
    public void doesNotRemoveSectionWithNonEmptyField() throws Exception {

        // ARRANGE
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        FieldMergingArgs args = Mockito.mock(FieldMergingArgs.class);
        Mockito.when(args.getFieldValue()).thenReturn("value");
        Mockito.when(args.getFieldName()).thenReturn("field");
        Field field = Mockito.mock(Field.class);
        FieldStart fieldStart = Mockito.mock(FieldStart.class);
        Node ancestor = Mockito.mock(Node.class);

        Mockito.when(args.getField()).thenReturn(field);
        Mockito.when(field.getStart()).thenReturn(fieldStart);
        Mockito.when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        Mockito.verify(ancestor, Mockito.never()).remove();
    }

    @Test
    public void ignoresEmptyFieldNotInFieldSet() throws Exception {

        // ARRANGE
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        FieldMergingArgs args = Mockito.mock(FieldMergingArgs.class);
        Mockito.when(args.getFieldValue()).thenReturn("");
        Mockito.when(args.getFieldName()).thenReturn("anotherfield");
        Field field = Mockito.mock(Field.class);
        FieldStart fieldStart = Mockito.mock(FieldStart.class);
        Node ancestor = Mockito.mock(Node.class);

        Mockito.when(args.getField()).thenReturn(field);
        Mockito.when(field.getStart()).thenReturn(fieldStart);
        Mockito.when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        Mockito.verify(ancestor, Mockito.never()).remove();
    }


    @Test
    public void imageFieldMergingDoesNothing() throws Exception {
        EmptySectionRemovingCallback sut = new EmptySectionRemovingCallback(Collections.singleton("field"));
        sut.imageFieldMerging(null);
    }
    // removes null fields

    // leaves other feidls


}
