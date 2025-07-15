package scot.mygov.housing.forms.foreigntraveldeclaration;

import org.junit.Test;
import scot.mygov.housing.forms.foreigntraveldeclaration.model.ForeignTravelDeclaration;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ForeignTravelDeclarationFieldExtractorTest {

    @Test
    public void setsReasonAsExpected() {
        ForeignTravelDeclarationFieldExtractor sut = new ForeignTravelDeclarationFieldExtractor();

        ForeignTravelDeclaration input = new ForeignTravelDeclaration();
        input.setDob(LocalDate.now());
        input.setSignedDate(LocalDate.now());
        input.setReason("WEDDING");
        Map<String, Object> output = sut.extractFields(input);

        assertEquals("X", output.get("WEDDING"));
        assertEquals("_", output.get("FUNERAL"));
    }
}
