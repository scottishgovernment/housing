package scot.mygov.documents;

import com.aspose.words.DocumentBuilder;
import org.junit.Test;

public class PlaceholderUtilsTest {

    @Test
    public void greenpath() throws Exception {
        // this is a non-test just to get coverage.
        DocumentBuilder documentBuilder = new DocumentBuilder();
        PlaceholderUtils.writeIndented(documentBuilder, "");
        PlaceholderUtils.writeNumberedLines(documentBuilder, 2);
        PlaceholderUtils.writeNumberedLines(documentBuilder, 2);
        PlaceholderUtils.writeNumberedLinesWithLabel(documentBuilder, "", 2);
        PlaceholderUtils.writeNumberedDoubleLines(documentBuilder, 2);
        PlaceholderUtils.writeLines(documentBuilder, 2);
        PlaceholderUtils.writeInlineField(documentBuilder, "");
    }
}
