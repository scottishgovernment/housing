package scot.mygov.housing.forms.nonprovisionofdocumentation;

import org.junit.Test;
import scot.mygov.housing.forms.nonprovisionofdocumentation.model.NonProvisionOfDocumentation;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static scot.mygov.housing.forms.nonprovisionofdocumentation.NonProvisionOfDocumentationFieldExtractor.CHECKED;
import static scot.mygov.housing.forms.nonprovisionofdocumentation.NonProvisionOfDocumentationFieldExtractor.UNCHECKED;


public class NonProvisionOfDocumentationFieldExtractorTest {


    NonProvisionOfDocumentationFieldExtractor sut = new NonProvisionOfDocumentationFieldExtractor();

    @Test
    public void section10FailureExtractedAsExpected() {

        // ARRANGE
        NonProvisionOfDocumentation input = new NonProvisionOfDocumentation();
        input.setSection10Failure(true);
        input.setSection10Details("section10 details");

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("section10Failure"), CHECKED);
        assertEquals(output.get("section10Details"), "section10 details");

    }

    @Test
    public void section10DetailsClearedIfNotChecked() {

        // ARRANGE
        NonProvisionOfDocumentation input = new NonProvisionOfDocumentation();
        input.setSection10Failure(false);
        input.setSection10Details("section10 details");

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("section10Failure"), UNCHECKED);
        assertEquals(output.get("section10Details"), "");
    }

    @Test
    public void section11FailureExtractedAsExpected() {

        // ARRANGE
        NonProvisionOfDocumentation input = new NonProvisionOfDocumentation();
        input.setSection11Failure(true);
        input.setSection11Details("section10 details");

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("section11Failure"), CHECKED);
        assertEquals(output.get("section11Details"), "section10 details");

    }

    @Test
    public void section11DetailsClearedIfNotChecked() {

        // ARRANGE
        NonProvisionOfDocumentation input = new NonProvisionOfDocumentation();
        input.setSection11Failure(false);
        input.setSection11Details("section11 details");

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("section11Failure"), UNCHECKED);
        assertEquals(output.get("section11Details"), "");
    }

    @Test
    public void section16FailureExtractedAsExpected() {

        // ARRANGE
        NonProvisionOfDocumentation input = new NonProvisionOfDocumentation();
        input.setSection16Failure(true);

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("section16Failure"), CHECKED);
    }

    @Test
    public void nonSection16FailureExtractedAsExpected() {

        // ARRANGE
        NonProvisionOfDocumentation input = new NonProvisionOfDocumentation();
        input.setSection16Failure(false);

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        assertEquals(output.get("section16Failure"), UNCHECKED);
    }
}
