package scot.mygov.housing.modeltenancy;

import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.Section;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Callback used in template service that will remove containing sections if sepecified fields are empty or null
 */
public class EmptySectionRemovingCallback implements IFieldMergingCallback {

    private final Set<String> fields;

    public EmptySectionRemovingCallback(Set<String> fields) {
        this.fields = fields;
    }

    @Override
    public void fieldMerging(FieldMergingArgs fieldMergingArgs) throws Exception {

        String fieldValue = fieldMergingArgs.getFieldValue() == null ?
                null : fieldMergingArgs.getFieldValue().toString();

        String fieldName = fieldMergingArgs.getFieldName();
        if (fields.contains(fieldName) && StringUtils.isEmpty(fieldValue)) {
            fieldMergingArgs.getField().getStart().getAncestor(Section.class).remove();
        }
    }

    @Override
    public void imageFieldMerging(ImageFieldMergingArgs imageFieldMergingArgs) throws Exception {
        // no action needed
    }
}