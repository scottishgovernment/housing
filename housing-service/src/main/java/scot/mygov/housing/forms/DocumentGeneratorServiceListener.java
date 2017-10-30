package scot.mygov.housing.forms;

import scot.mygov.documents.DocumentGeneratorException;

public interface DocumentGeneratorServiceListener<T> {

    void onGenerateStart(T model);

    void onGenerateDone(T model, byte [] docBytes);

    void onGenerateException(T model, DocumentGeneratorException e);
}
