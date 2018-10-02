package scot.mygov.housing.forms.rentincreasenotice;

import com.aspose.words.Field;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.FieldStart;
import com.aspose.words.Section;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RentIncreaseRPZSectionRemovingCallbackTest {

    RentIncreaseRPZSectionRemovingCallback sut = new RentIncreaseRPZSectionRemovingCallback();

    @Test
    public void ignoresOtherFields() throws Exception {

        // ARRANGE
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldName()).thenReturn("something");

        // ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(args, never()).getFieldValue();
    }

    @Test
    public void inRPZDoesNotRemoveSection() throws Exception {

        // ARRANGE
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        Section section = mock(Section.class);
        Field field = fieldWithSection(section);
        when(args.getFieldName()).thenReturn("inRPZ");
        when(args.getFieldValue()).thenReturn("in");
        when(args.getField()).thenReturn(field);

        // ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(section, never()).remove();
    }

    @Test
    public void notInRPZRemovesSection() throws Exception {

        // ARRANGE
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        Section section = mock(Section.class);
        Field field = fieldWithSection(section);
        when(args.getFieldName()).thenReturn("inRPZ");
        when(args.getFieldValue()).thenReturn("not in");
        when(args.getField()).thenReturn(field);

        // ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(section).remove();
    }

    Field fieldWithSection(Section section) {
        Field field = mock(Field.class);
        FieldStart start = mock(FieldStart.class);
        when(field.getStart()).thenReturn(start);
        when(start.getAncestor(any())).thenReturn(section);
        return field;
    }
}
