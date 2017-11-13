package scot.mygov.housing.forms;

import scot.mygov.documents.DocumentGeneratorException;

public class DocumentGeneratorServiceListenerAdaptor<T> implements DocumentGeneratorServiceListener<T> {

    public void onGenerateStart(T model) {
        // do nothing
    }

    public void onGenerateDone(T model, byte [] docBytes) {
        // do nothing
    }

    public void onGenerateException(T model, DocumentGeneratorException e) {
        // do nothing
    }

}
