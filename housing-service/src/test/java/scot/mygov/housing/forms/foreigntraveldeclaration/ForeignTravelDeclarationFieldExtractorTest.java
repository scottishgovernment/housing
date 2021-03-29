package scot.mygov.housing.forms.foreigntraveldeclaration;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ForeignTravelDeclarationFieldExtractorTest {

    @Test
    public void setsReasonAsExpected() {
        ForeignTravelDeclarationFieldExtractor sut = new ForeignTravelDeclarationFieldExtractor();

        ForeignTravelDeclaration input = new ForeignTravelDeclaration();
        input.setReason("WEDDING");
        Map<String, Object> output = sut.extractFields(input);

        assertEquals(output.get("WEDDING"), "X");
        assertEquals(output.get("FUNERAL"), "_");
    }
}
