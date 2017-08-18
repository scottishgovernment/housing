package scot.mygov.housing.postcode;

public class PostcodeServiceException extends Exception {

    public PostcodeServiceException(String msg) {
        super(msg);
    }

    public PostcodeServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

