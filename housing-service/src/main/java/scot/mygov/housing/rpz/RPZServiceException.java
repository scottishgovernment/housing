package scot.mygov.housing.rpz;

public class RPZServiceException extends Exception {

    public RPZServiceException(String msg) {
        super(msg);
    }

    public RPZServiceException(String msg, Throwable t) {
        super(msg, t);
    }
}
