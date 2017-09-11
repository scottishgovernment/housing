package scot.mygov.housing.rpz;

import java.util.Map;

/**
 * RPZService exception used to iundicate a client error.
 */
public class RPZServiceClientException extends RPZServiceException {

    private final Map<String, String> errors;

    public RPZServiceClientException(Map<String, String> errors) {
        super("Invalid request");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
