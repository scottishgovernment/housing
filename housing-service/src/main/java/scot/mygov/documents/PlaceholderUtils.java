package scot.mygov.documents;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.ParagraphFormat;

import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;

public class PlaceholderUtils {

    public static final String DATE_PLACEHOLDER = "__/__/__";
    public static final String MONETARY_PLACEHOLDER = "____.__";

    private PlaceholderUtils() {
        // no not create instance
    }

    public static void put(
            Map<String, Consumer<DocumentBuilder>> placeholders,
            Consumer<DocumentBuilder> consumer,
            String ...fields) {
        for (String field : fields) {
            placeholders.put(field, consumer);
        }
    }

    public static Consumer<DocumentBuilder> lines(int i) {
        return db -> writeLines(db, i);
    }

    public static Consumer<DocumentBuilder> inline(String placeholder) {
        return db -> writeInlineField(db, placeholder);
    }

    public static Consumer<DocumentBuilder> numberedLines(int i) {
        return db -> writeNumberedLines(db, i);
    }

    public static Consumer<DocumentBuilder> numberedLines(String prefix, String postfix, int i) {
        return db -> writeNumberedLines(db, prefix, postfix, i);
    }

    public static Consumer<DocumentBuilder> numberedLinesWithLabel(String label, int i) {
        return db -> writeNumberedLinesWithLabel(db, label, i);
    }

    public static Consumer<DocumentBuilder> numberedDoubleLines(int i) {
        return db -> writeNumberedDoubleLines(db, i);
    }

    public static Consumer<DocumentBuilder> inlineDate() {
        return db -> writeInlineField(db, DATE_PLACEHOLDER);
    }

    public static Consumer<DocumentBuilder> inlineMonetaryValue() {
        return db -> writeInlineField(db, MONETARY_PLACEHOLDER);
    }

    public static void writeIndented(DocumentBuilder builder, String txt) {
        builder.getParagraphFormat().setLeftIndent(20);
        builder.writeln(txt);
        builder.getParagraphFormat().setLeftIndent(0);
    }

    private static void writeNumberedDoubleLines(DocumentBuilder builder, int n) {
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

    private static void writeInlineField(DocumentBuilder builder, String field) {
        builder.getFont().getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        builder.write(field);
    }

    private static void writeNumberedLines(DocumentBuilder builder, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln("(" + i + ")\t  \t\t");
        }
    }

    private static void writeNumberedLinesWithLabel(DocumentBuilder builder, String label, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln(String.format("%s (%d) \t\t", label, i));
        }
    }

    private static void writeNumberedLines(DocumentBuilder builder, String prefix, String postfix, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln(prefix + "(" + i + ")\t  \t\t" + postfix);
        }
    }
}
