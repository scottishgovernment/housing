package scot.mygov.housing.forms.rentincreaseforimprovementsnotice;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.rentincreaseforimprovementsnotice.model.RentIncreaseForImprovements;

import java.util.Map;

public class RentIncreaseForImprovementsFeildExtractorTest {

    RentIncreaseForImprovementsFieldExtractor sut = new RentIncreaseForImprovementsFieldExtractor();

    @Test
    public void includesReceiptsExtractedCorrectlyWhenTrue() {
        RentIncreaseForImprovements input = new RentIncreaseForImprovements();
        input.setIncludesReceipts(true);
        Map<String, Object> output = sut.extractFields(input);
        Assert.assertEquals("X", output.get("includesReceipts"));
    }

    @Test
    public void includesReceiptsExtractedCorrectlyWhenFalse() {
        RentIncreaseForImprovements input = new RentIncreaseForImprovements();
        input.setIncludesReceipts(false);
        Map<String, Object> output = sut.extractFields(input);
        Assert.assertEquals("_", output.get("includesReceipts"));
    }

    @Test
    public void includesBeforeAndAfterPicturesExtractedCorrectlyWhenTrue() {
        RentIncreaseForImprovements input = new RentIncreaseForImprovements();
        input.setIncludesBeforeAndAfterPictures(true);
        Map<String, Object> output = sut.extractFields(input);
        Assert.assertEquals("X", output.get("includesBeforeAndAfterPictures"));
    }

    @Test
    public void includesBeforeAndAfterPicturesExtractedCorrectlyWhenFalse() {
        RentIncreaseForImprovements input = new RentIncreaseForImprovements();
        input.setIncludesBeforeAndAfterPictures(false);
        Map<String, Object> output = sut.extractFields(input);
        Assert.assertEquals("_", output.get("includesBeforeAndAfterPictures"));
    }
}
