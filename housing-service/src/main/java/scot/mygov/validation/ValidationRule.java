package scot.mygov.validation;

public interface ValidationRule<T> {

    void validate(T model, ValidationResultsBuilder resultsBuilder);
}
