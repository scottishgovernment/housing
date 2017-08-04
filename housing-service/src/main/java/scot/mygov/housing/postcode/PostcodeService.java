package scot.mygov.housing.postcode;

public interface PostcodeService {

    PostcodeServiceResults lookup(String postcode) throws PostcodeServiceException;

}
