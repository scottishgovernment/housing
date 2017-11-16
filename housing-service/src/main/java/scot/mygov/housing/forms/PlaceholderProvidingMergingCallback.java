package scot.mygov.housing.forms;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

public class PlaceholderProvidingMergingCallback implements IFieldMergingCallback {

    private final Map<String, Consumer<DocumentBuilder>> placeholders;

    public PlaceholderProvidingMergingCallback(Map<String, Consumer<DocumentBuilder>> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public void fieldMerging(FieldMergingArgs fieldMergingArgs) throws Exception {
        String fieldValue = fieldMergingArgs.getFieldValue() == null ?
                null : fieldMergingArgs.getFieldValue().toString();
        String fieldName = fieldMergingArgs.getFieldName();

        // do we want to provide a placeholder for an empty value?
        if (placeholders.containsKey(fieldName) && StringUtils.isEmpty(fieldValue)) {
            DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
            builder.moveToMergeField(fieldName);
            placeholders.get(fieldMergingArgs.getFieldName()).accept(builder);
            return;
        }
    }

    @Override
    public void imageFieldMerging(ImageFieldMergingArgs var1) throws Exception {
        // no nothing
    }
}
