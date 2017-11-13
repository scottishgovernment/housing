package scot.mygov.housing.forms;

import com.aspose.words.IFieldMergingCallback;

public interface IFieldMergingCallbackFactory<T> {

    IFieldMergingCallback newCallback(T formData);

}
