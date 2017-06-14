package scot.mygov.housing.rpz;

import junit.framework.Assert;
import org.junit.Test;
import scot.mygov.housing.rpz.PostcodeSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PostcodeSourceTest {


    @Test
    public void validPostCodes() {
        PostcodeSource sut = new PostcodeSource(null);
        List<String> inputs = new ArrayList<>();
        Collections.addAll(inputs, "EH104AX", "EH10 4AX", "eh104ax", "eh10 4ax", "B1 2HB");
        List<String> outputs = inputs.stream().filter(in -> sut.validPostcode(in)).collect(Collectors.toList());
        Assert.assertEquals(inputs, outputs);
    }

    @Test
    public void invalidPostCodes() {
        PostcodeSource sut = new PostcodeSource(null);
        List<String> inputs = new ArrayList<>();
        Collections.addAll(inputs, "", "aaa", "EH10-$AX");
        List<String> outputs = inputs.stream().filter(in -> !sut.validPostcode(in)).collect(Collectors.toList());
        Assert.assertEquals(inputs, outputs);
    }
}
