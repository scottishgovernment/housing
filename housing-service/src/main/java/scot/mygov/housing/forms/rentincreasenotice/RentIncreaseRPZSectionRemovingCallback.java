package scot.mygov.housing.forms.rentincreasenotice;

import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.Section;

public class RentIncreaseRPZSectionRemovingCallback implements IFieldMergingCallback {

    public void fieldMerging(FieldMergingArgs args) throws Exception {

        String name = args.getFieldName();

        if (!"inRPZ".equals(name)) {
            return;
        }

        String value =
                args.getFieldValue() == null
                ? null
                : args.getFieldValue().toString();
        //
        if ("in".equals(value)) {
            return;
        }

        // this property is not in an RPZ so remove the section.
        Section section = (Section) args.getField().getStart().getAncestor(Section.class);
        section.remove();
    }

    public void imageFieldMerging(ImageFieldMergingArgs var1) throws Exception {
        // nothing
    }
}
