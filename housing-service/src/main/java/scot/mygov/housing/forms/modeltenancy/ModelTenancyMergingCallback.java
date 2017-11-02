package scot.mygov.housing.forms.modeltenancy;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.ParagraphFormat;
import com.aspose.words.Section;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.forms.InitialisationFailedException;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Collections.addAll;

public class ModelTenancyMergingCallback implements IFieldMergingCallback {

    private static String PLACEHOLDER_HTML =
            "<p>Your landlord has used their own wording for this clause.</p>" +
            "<p>If you need more information about this clauses you may want to discuss them with your landlord, " +
            "or contact the advice groups listed at the end of these Notes.</p>";

    // the name of fields that will cause their section to be removed if they are empty
    private static final Set<String> fieldsToRemoveIfEmpty = fieldsToDeleteIfEmpty();
    private static final OptionalTerms defaultTerms = OptionalTermsUtil.defaultTerms();
    private static final OptionalTerms defaultNotes = OptionalTermsUtil.defaultEasyreadNotes();

    private final ModelTenancy tenancy;

    private Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();

    public ModelTenancyMergingCallback(ModelTenancy tenancy) {
        this.tenancy = tenancy;
        populatePlaceholders();
    }

    public void populatePlaceholders() {
        String datePlaceholder = "__/__/__";
        String monetaryPlaceholder = "____.__";
        placeholders.put("tenantNamesAndAddresses", documentBuilder -> writeNumberedDoubleLines(documentBuilder, 5));
        placeholders.put("tenantEmails", documentBuilder -> writeNumberedLines(documentBuilder, 5));
        placeholders.put("tenantPhoneNumbers", documentBuilder -> writeNumberedLines(documentBuilder, 5));
        placeholders.put("lettingAgentName", documentBuilder -> writeLines(documentBuilder, 1));
        placeholders.put("lettingAgentAddress", documentBuilder -> writeLines(documentBuilder, 3));
        placeholders.put("lettingAgentEmail", documentBuilder -> writeLines(documentBuilder, 1));
        placeholders.put("lettingAgentPhone", documentBuilder -> writeLines(documentBuilder, 1));
        placeholders.put("lettingAgentRegistrationNumber", documentBuilder -> writeLines(documentBuilder, 1));
        placeholders.put("lettingAgentServices", documentBuilder -> writeLines(documentBuilder, 3));
        placeholders.put("lettingAgentPointOfContactServices", documentBuilder -> writeLines(documentBuilder, 3));
        placeholders.put("landlordNames", documentBuilder -> writeNumberedLines(documentBuilder, 2));
        placeholders.put("landlordAddresses", documentBuilder -> writeNumberedLines(documentBuilder, 6));
        placeholders.put("landlordEmails", documentBuilder -> writeNumberedLines(documentBuilder, 2));
        placeholders.put("landlordPhones", documentBuilder -> writeNumberedLines(documentBuilder, 2));
        placeholders.put("landlordRegNumbers",
                documentBuilder -> writeNumberedLinesWithLabel(documentBuilder, "Landlord Registration number ", 2));
        placeholders.put("propertyAddress", documentBuilder -> writeLines(documentBuilder, 3));
        placeholders.put("propertyType", documentBuilder -> writeLines(documentBuilder, 1));
        placeholders.put("includedAreasOrFacilities", documentBuilder -> writeLines(documentBuilder, 2));
        placeholders.put("sharedFacilities", documentBuilder -> writeLines(documentBuilder, 2));
        placeholders.put("excludedAreasFacilities", documentBuilder -> writeLines(documentBuilder, 2));
        placeholders.put("furnishingType",
                documentBuilder -> writeInlineField(documentBuilder, "[Furnished / Unfurnished]"));
        placeholders.put("hmoString", documentBuilder -> writeInlineField(documentBuilder, "[is / is not]"));
        placeholders.put("hmoContactNumber", documentBuilder -> writeLines(documentBuilder, 2));
        placeholders.put("hmoExpiryDate", documentBuilder -> writeInlineField(documentBuilder, datePlaceholder));
        placeholders.put("tenancyStartDate", documentBuilder -> writeInlineField(documentBuilder, datePlaceholder));
        placeholders.put("rentAmount", documentBuilder -> writeInlineField(documentBuilder, monetaryPlaceholder));
        placeholders.put("rentPressureZoneString", documentBuilder -> writeInlineField(documentBuilder,
                "[is / is not]"));
        placeholders.put("rentPaymentFrequency", documentBuilder -> writeInlineField(documentBuilder,
                "[week/fortnight/four weeks/calendar month/quarter/year]"));
        placeholders.put("servicesIncludedInRent", documentBuilder -> writeLines(documentBuilder, 3));
        placeholders.put("firstPaymentDate", documentBuilder -> writeInlineField(documentBuilder, datePlaceholder));
        placeholders.put("advanceOrArrears",
                documentBuilder -> writeInlineField(documentBuilder, "[advance / arears]"));
        placeholders.put("firstPaymentAmount",
                documentBuilder -> writeInlineField(documentBuilder, monetaryPlaceholder));
        placeholders.put("firstPaymentPeriodStart",
                documentBuilder -> writeInlineField(documentBuilder, datePlaceholder));
        placeholders.put("firstPaymentPeriodEnd",
                documentBuilder -> writeInlineField(documentBuilder, datePlaceholder));
        placeholders.put("rentPaymentDayOrDate", documentBuilder -> writeInlineField(documentBuilder, "__________"));
        placeholders.put("rentPaymentSchedule",
                documentBuilder -> writeInlineField(documentBuilder,
                        "[day of each week/fortnight/four weekly period/date each calendar month/date each 6-month period]"));
        placeholders.put("rentPaymentMethod", documentBuilder -> writeInlineField(documentBuilder, "__________"));
        placeholders.put("guarentorSignatures", documentBuilder -> {
            writeInlineField(documentBuilder, "Guarantor signature(s)");
            writeLines(documentBuilder, 15);
        });
        placeholders.put("tenantSignatures", documentBuilder -> {
            writeInlineField(documentBuilder, "Tenant signature(s)");
            writeLines(documentBuilder, 15);
        });
        placeholders.put("landlordSignatures", documentBuilder -> {
            writeInlineField(documentBuilder, "Landlord signature(s)");
            writeLines(documentBuilder, 15);
        });
    }

    public void writeNumberedLines(DocumentBuilder builder, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln("(" + i + ")\t  \t\t");
        }
    }

    public void writeNumberedLinesWithLabel(DocumentBuilder builder, String label, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln(String.format("%s (%d) \t\t", label, i));
        }
    }

    public void writeNumberedDoubleLines(DocumentBuilder builder, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln("(" + i + ")\t  \t\t\n\t\t");
        }
    }

    public void writeLines(DocumentBuilder builder, int n) {
        ParagraphFormat paragraphFormat = builder.getParagraphFormat();
        paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= n; i++) {
            builder.writeln("\t \t\t");
        }
    }

    public void writeInlineField(DocumentBuilder builder, String field) {
        builder.getFont().getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
        builder.write(field);
    }

    @Override
    public void fieldMerging(FieldMergingArgs fieldMergingArgs) throws Exception {

        String fieldValue = fieldMergingArgs.getFieldValue() == null ?
                null : fieldMergingArgs.getFieldValue().toString();
        String fieldName = fieldMergingArgs.getFieldName();

        // do we want to provide a placeholder for an empty value?
        if (placeholders.containsKey(fieldName) && StringUtils.isEmpty(fieldValue)) {
            provdePlacholder(fieldMergingArgs);
            return;
        }

        // if the field is one of the fieldsToRemoveIfEmpty then remove the sections it is contained within from the
        // document.
        if (fieldsToRemoveIfEmpty.contains(fieldName) && StringUtils.isEmpty(fieldValue)) {
            Section section = (Section) fieldMergingArgs.getField().getStart().getAncestor(Section.class);
            section.remove();
            return;
        }

        // For any field ending in EasyreadNotes we need to decide wether to :
        //
        // 1/ include the note (if the term has not been changed by the user)
        // 2/ remove note altogether oif the user removed the term
        // 3/ put in placeholder text to notify the user fo the change
        //
        // In addition the utilities field is handled slightly differently - changes to the list of utilities is not
        // considered a change in the above logic.
        if (fieldName.endsWith("EasyreadNotes")) {
            String termName = StringUtils.substringBeforeLast(fieldName, "EasyreadNotes");
            String value = BeanUtils.getProperty(tenancy.getOptionalTerms(), termName);
            String defaultValue = BeanUtils.getProperty(defaultTerms, termName);

            if (StringUtils.isEmpty(value)) {
                // user removed the term, remove the section
                Section section = (Section) fieldMergingArgs.getField().getStart().getAncestor(Section.class);
                section.remove();
                return;
            }

            String html;
            if ("utilities".equals(termName)) {
                // special case for utilities field.
                html = easyreadNotesForUtilities(value, BeanUtils.getProperty(defaultNotes, termName));
            } else {
                // default to using the easytread notes for this field.
                html = BeanUtils.getProperty(defaultNotes, termName);

                if (!value.equals(defaultValue)){
                    // they chnged the value, use the placeholder html.
                    html = PLACEHOLDER_HTML;
                }
            }

            // insert the relevant content into the document.
            DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
            builder.moveToMergeField(fieldName);

            builder.insertHtml("<font face=\"arial\">" + html + "</font>");
        }
    }

    public void provdePlacholder(FieldMergingArgs fieldMergingArgs) throws Exception {
        DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
        builder.moveToMergeField(fieldMergingArgs.getFieldName());
        placeholders.get(fieldMergingArgs.getFieldName()).accept(builder);
    }


    /**
     * The utilities term contains a placeholder within []'s.
     */
    public static String easyreadNotesForUtilities(String utilitiesTerm, String defaultTerm) {
        String utilsList = "[gas/electricity/telephone/TV licence/internet/broadband]";
        String defaultUtilitiesTerm = OptionalTermsUtil.defaultTerms().getUtilities();
        String prefix = org.apache.commons.lang.StringUtils.substringBefore(defaultUtilitiesTerm, utilsList);
        String postfix = org.apache.commons.lang.StringUtils.substringAfter(defaultUtilitiesTerm, utilsList);
        if (utilitiesTerm.startsWith(prefix) && utilitiesTerm.endsWith(postfix)) {
            // inject the utilities from the term into the easyreadnotes
            String utilities = StringUtils.substringBetween(utilitiesTerm, prefix, postfix);
            return defaultTerm.replace("[]", utilities);
        }

        return PLACEHOLDER_HTML;
    }

    @Override
    public void imageFieldMerging(ImageFieldMergingArgs imageFieldMergingArgs) throws Exception {
        // no action needed
    }

    private static final Set<String> fieldsToDeleteIfEmpty() {
        Set<String> fields = new HashSet<>();
        // add al of the optional sections.
        try {
            fields.addAll(BeanUtils.describe(new OptionalTerms()).keySet());

            // other fields with sections
            addAll(fields,
                    "communicationsAgreementType",
                    "showHmoNotification",
                    "showHmoFields",
                    "showEmailParagraphs");
            return fields;
        } catch (Exception e) {
            throw new InitialisationFailedException("Failed to extract optional section fields", e);
        }
    }
}