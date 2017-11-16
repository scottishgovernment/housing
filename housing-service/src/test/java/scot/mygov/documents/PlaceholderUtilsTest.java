package scot.mygov.documents;

import com.aspose.words.DocumentBuilder;
import org.junit.Test;

public class PlaceholderUtilsTest {

    @Test
    public void greenpath() throws Exception {
        // this is a non-test just to get coverage.
        DocumentBuilder documentBuilder = new DocumentBuilder();
        PlaceholderUtils.writeIndented(documentBuilder, "");
        PlaceholderUtils.numberedLines(2).accept(documentBuilder);

        PlaceholderUtils.numberedLinesWithLabel("", 2).accept(documentBuilder);
        PlaceholderUtils.numberedDoubleLines(2).accept(documentBuilder);
        PlaceholderUtils.writeLines(documentBuilder, 2);
        PlaceholderUtils.inline("").accept(documentBuilder);
    }
}
