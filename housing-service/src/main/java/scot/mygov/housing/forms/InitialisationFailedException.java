package scot.mygov.housing.forms;

/**
 * Runtime exception used to indicate that the service could not start.  Used for fail early berhaviour for conditions
 * that are not expected in real life.
 */
public class InitialisationFailedException extends RuntimeException {

    public InitialisationFailedException(String msg, Throwable t) {
        super(msg, t);
    }

}
