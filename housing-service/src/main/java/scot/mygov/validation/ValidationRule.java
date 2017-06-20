package scot.mygov.validation;

/**
 * Created by z418868 on 15/06/2017.
 */
public interface ValidationRule<T> {

    void validate(T model, ValidationResultsBuilder resultsBuilder);
}
