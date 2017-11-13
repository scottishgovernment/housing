package scot.mygov.housing.forms.modeltenancy;

import com.aspose.words.Document;
import com.aspose.words.Field;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.FieldStart;
import com.aspose.words.Node;
import com.aspose.words.Section;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.Term;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyObjectMother;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModelTenancyMergingCallbackTest {

    ModelTenancyObjectMother om = new ModelTenancyObjectMother();

    @Test
    public void removesSectionWithEmptyField() throws Exception {

        // ARRANGE
        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(om.anyTenancy());
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("roof");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(ancestor).remove();
    }

    @Test
    public void removesSectionWithNullField() throws Exception {

        // ARRANGE
        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(om.anyTenancy());
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn(null);
        when(args.getFieldName()).thenReturn("roof");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

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
        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(om.anyTenancy());
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
        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(om.anyTenancy());
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
    public void additionalTermsFormattedCorrectly() throws Exception {

        // ARRANGE
        Term term = new Term();
        term.setContent("title");
        term.setContent("content");
        ModelTenancy tenancy = om.anyTenancy();
        tenancy.setAdditionalTerms(Collections.singletonList(term));

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldName()).thenReturn("additionalTerms");
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void emptyEasyreadNotesRemoved() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();
        tenancy.getOptionalTerms().setUtilities("");
        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("utilitiesEasyreadNotes");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
        verify(ancestor, Mockito.atLeastOnce()).remove();
    }

    @Test
    public void utilitiesEasyreadNotesUsePlaceholderForChangedNotes() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();
        tenancy.getOptionalTerms().setUtilities("changed");

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(om.anyTenancy());
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("changed");
        when(args.getFieldName()).thenReturn("utilitiesEasyreadNotes");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void guarentorsSignatureBlock() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.tenancyWithGuarentors();;

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("guarentorSignatures");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void tenantSignatureBlock() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();;

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("tenantSignatures");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void landlordSignatureBlock() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();;

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("landlordSignatures");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void tenantEmails() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();;

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("tenantEmails");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void landlordNames() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();;

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("landlordNames");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void notificationResidents() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();;

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("notificationResidents");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void tenantNamesAndAddresses() throws Exception {

        // ARRANGE
        ModelTenancy tenancy = om.anyTenancy();;

        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);
        FieldMergingArgs args = mock(FieldMergingArgs.class);
        when(args.getFieldValue()).thenReturn("");
        when(args.getFieldName()).thenReturn("tenantNamesAndAddresses");
        Field field = mock(Field.class);
        FieldStart fieldStart = mock(FieldStart.class);
        Node ancestor = mock(Section.class);

        when(args.getField()).thenReturn(field);
        when(field.getStart()).thenReturn(fieldStart);
        when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
        when(args.getDocument()).thenReturn(new Document());

        //  ACT
        sut.fieldMerging(args);

        // ASSERT
    }

    @Test
    public void imageFieldMergingDoesNothing() throws Exception {
        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(om.anyTenancy());
        sut.imageFieldMerging(null);
    }

    @Test
    public void emptyFields() throws Exception {

        Set<String> fields = new HashSet<>();
        Collections.addAll(fields, "advanceOrArrears",
                "landlordRegNumbers", "furnishingType", "utilitiesEasyreadNotes",
                "firstPaymentPeriodStart", "firstPaymentPeriodEnd", "rentPaymentSchedule", "firstPaymentAmount",
                "tenantSignatures", "landlordSignatures", "guarentorSignatures");

        ModelTenancy tenancy = om.emptyTenancy();
        ModelTenancyMergingCallback sut = new ModelTenancyMergingCallback(tenancy);

        for (String fieldname : fields) {
            FieldMergingArgs args = mock(FieldMergingArgs.class);
            when(args.getFieldValue()).thenReturn("");
            when(args.getFieldName()).thenReturn(fieldname);
            Field field = mock(Field.class);
            FieldStart fieldStart = mock(FieldStart.class);
            Node ancestor = mock(Section.class);

            when(args.getField()).thenReturn(field);
            when(field.getStart()).thenReturn(fieldStart);
            when(fieldStart.getAncestor(ArgumentMatchers.any())).thenReturn(ancestor);
            when(args.getDocument()).thenReturn(new Document());

            when(args.getFieldValue()).thenReturn("");

            sut.fieldMerging(args);
        }





    }
}
