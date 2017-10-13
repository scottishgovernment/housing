package scot.mygov;

/**
 * Exception thrown by the housing service if a know resource is not available on startup. This should never be thrown
 * in the wild.
 */
public class UnavailableResourceException extends RuntimeException {

    public UnavailableResourceException(String msg, Throwable t) {
         super(msg, t);
    }

}
