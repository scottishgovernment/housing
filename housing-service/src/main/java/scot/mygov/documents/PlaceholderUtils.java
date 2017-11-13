package scot.mygov.documents;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.ParagraphFormat;

import java.awt.*;

public class PlaceholderUtils {

    public static final String DATE_PLACEHOLDER = "__/__/__";
    public static final String MONETARY_PLACEHOLDER = "____.__";

    private PlaceholderUtils() {
        // no not create instance
    }

    public static void writeIndented(DocumentBuilder builder, String txt) {
        builder.getParagraphFormat().setLeftIndent(20);
        builder.writeln(txt);
        builder.getParagraphFormat().setLeftIndent(0);
    }

    public static void writeNumberedLines(DocumentBuilder builder, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln("(" + i + ")\t  \t\t");
        }
    }

    public static void writeNumberedLines(DocumentBuilder builder, String prefix, String postfix, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln(prefix + "(" + i + ")\t  \t\t" + postfix);
        }
    }

    public static void writeNumberedLinesWithLabel(DocumentBuilder builder, String label, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln(String.format("%s (%d) \t\t", label, i));
        }
    }

    public static void writeNumberedDoubleLines(DocumentBuilder builder, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln("(" + i + ")\t  \t\t\n\t\t");
        }
    }

    public static void writeLines(DocumentBuilder builder, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln("\t \t\t");
        }
    }

    public static void writeInlineField(DocumentBuilder builder, String field) {
        builder.getFont().getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        builder.write(field);
    }
}
