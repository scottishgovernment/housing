package scot.mygov.housing.forms.modeltenancy;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.ParagraphFormat;
import com.aspose.words.Section;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import scot.mygov.housing.forms.FieldExtractorUtils;
import scot.mygov.housing.forms.InitialisationFailedException;
import scot.mygov.housing.forms.modeltenancy.model.Guarantor;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;
import scot.mygov.housing.forms.modeltenancy.model.Person;
import scot.mygov.housing.forms.modeltenancy.model.Term;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Collections.addAll;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static scot.mygov.documents.PlaceholderUtils.*;

import static scot.mygov.housing.forms.modeltenancy.ModelTenancyFieldExtractor.NEWLINE;

public class ModelTenancyMergingCallback implements IFieldMergingCallback {

    private static String EASYREAD_PLACEHOLDER_HTML =
            "<p>Your landlord has used their own wording for this clause.</p>" +
            "<p>If you need more information about this clauses you may want to discuss them with your landlord, " +
            "or contact the advice groups listed at the end of these Notes.</p>";

    private static final String ALTERATIONS = "alterations";
    private static final String DATE_LABEL = "Date:";
    private static final String UTILITIES_LIST = "[gas/electricity/telephone/TV licence/internet/broadband]";
    // the name of fields that will cause their section to be removed if they are empty
    private static final Set<String> fieldsToRemoveIfEmpty = fieldsToDeleteIfEmpty();
    private static final OptionalTerms defaultTerms = TermsUtil.defaultOptionalTerms();
    private static final OptionalTerms defaultNotes = TermsUtil.defaultEasyreadNotes();

    private final ModelTenancy tenancy;

    private Map<String, Consumer<DocumentBuilder>> placeholders = new HashMap<>();

    public ModelTenancyMergingCallback(ModelTenancy tenancy) {
        this.tenancy = tenancy;
        populatePlaceholders();
    }

    public void populatePlaceholders() {
        placeholders.put("tenantNamesAndAddresses", numberedDoubleLines(5));
        placeholders.put("tenantEmails", numberedLines(5));
        placeholders.put("tenantPhoneNumbers", numberedLines(5));
        placeholders.put("lettingAgentName", lines(1));
        placeholders.put("lettingAgentAddress", lines(3));
        placeholders.put("lettingAgentEmail", lines(1));
        placeholders.put("lettingAgentPhone", lines(1));
        placeholders.put("lettingAgentRegistrationNumber", lines(1));
        placeholders.put("lettingAgentServices", lines(3));
        placeholders.put("lettingAgentPointOfContactServices", lines(3));
        placeholders.put("landlordNames", numberedLines("Name ", "\n\n\n", 2));
        placeholders.put("landlordAddresses", numberedLines("Address ", "\n\n\n", 2));
        placeholders.put("landlordEmails", numberedLines(2));
        placeholders.put("landlordPhones", numberedLines(2));
        placeholders.put("landlordRegNumbers", numberedLinesWithLabel("Landlord Registration number ", 2));
        placeholders.put("propertyAddress", lines(3));
        placeholders.put("propertyType", lines(1));
        placeholders.put("includedAreasOrFacilities",lines(2));
        placeholders.put("sharedFacilities", lines(2));
        placeholders.put("excludedAreasFacilities", lines(2));
        placeholders.put("furnishingType", inline("[Furnished / Unfurnished / Partly furnished]"));
        placeholders.put("hmoString", inline("[is / is not]"));
        placeholders.put("hmoContactNumber", lines(2));
        placeholders.put("hmoExpiryDate", inlineDate());
        placeholders.put("tenancyStartDate", inlineDate());
        placeholders.put("depositAmount", inlineMonetaryValue());
        placeholders.put("depositSchemeAdministrator", inline("______________________________"));
        placeholders.put("depositSchemeContactDetails", lines(4));
        placeholders.put("rentAmount", inlineMonetaryValue());
        placeholders.put("originalRentAmount", inlineMonetaryValue());
        placeholders.put("rentPressureZoneString", inline("[is / is not]"));
        placeholders.put("servicesIncludedInRent", lines(3));
        placeholders.put("firstPaymentDate", inlineDate());
        placeholders.put("advanceOrArrears", inline("[advance / arears]"));
        placeholders.put("firstPaymentAmount", inlineMonetaryValue());
        placeholders.put("firstPaymentPeriodStart", inlineDate());
        placeholders.put("firstPaymentPeriodEnd", inlineDate());
        placeholders.put("rentPaymentFrequencyDayOrDate", inline("__________"));
        placeholders.put("rentPaymentSchedule", inline("[day of each week/fortnight/four weekly period/date each calendar month/date each 3-month period/date each 6-month period]"));

        placeholders.put("rentPaymentMethod", inline("__________"));
        placeholders.put("rentPaymentFrequency", inline("[week/fortnight/four weeks/calendar month/quarter/six months]"));
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

        // handle guarentors section:
        handleSignatureBlocks(fieldName, fieldMergingArgs);

        // special case for additional terms so that we can insert some html...
        if ("additionalTerms".equals(fieldName)) {
            handleAdditionalTerms(fieldMergingArgs, fieldName);
            return;
        }

        // special case for utilities - give them a grey background if the user has not edited them...
        if ("utilities".equals(fieldName) && !StringUtils.isEmpty(fieldValue)) {
            DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
            builder.moveToMergeField(fieldName);
            String withGreyBackground = "<span style=\"background-color:lightgrey\">" + UTILITIES_LIST + "</span>";
            String val = fieldValue.replace(UTILITIES_LIST, withGreyBackground);
            insertHtml(val, builder);
        }

        // if the field is one of the fieldsToRemoveIfEmpty then remove the sections it is contained within from the
        // document.
        if (shouldRemoveSection(fieldName, fieldValue, fieldMergingArgs)) {
            Section section = (Section) fieldMergingArgs.getField().getStart().getAncestor(Section.class);
            section.remove();
            return;
        }

        // special case for notificationResidents
        if ("notificationResidents".equals(fieldName)) {
            handleNotificationResidents(fieldMergingArgs, fieldName);
        }

        handleEasyreadNotes(fieldName, fieldMergingArgs);
    }

    @Override
    public void imageFieldMerging(ImageFieldMergingArgs imageFieldMergingArgs) throws Exception {
        // no action needed
    }

    private void handleAdditionalTerms(FieldMergingArgs fieldMergingArgs, String fieldName) throws Exception {
        if (tenancy.getAdditionalTerms().isEmpty()) {
            Section section = (Section) fieldMergingArgs.getField().getStart().getAncestor(Section.class);
            section.remove();
        } else {
            String html = formatAdditionalTerms(tenancy);
            DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
            builder.moveToMergeField(fieldName);
            insertHtml(html, builder);
        }
    }

    private void handleNotificationResidents(FieldMergingArgs fieldMergingArgs, String fieldName)  throws Exception {
        // if they have changed the term from the deault then add a paragraph break before the altered text.
        String injectValue = tenancy.getMustIncludeTerms().getNotificationResidents();
        //StringUtils.difference(TermsUtil.defaultMustIncludeTerms().getNotificationResidents(), )
        if (!TermsUtil.defaultMustIncludeTerms().getNotificationResidents().equals(injectValue)) {
            injectValue = "</br></br>" + injectValue;
        }
        DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
        builder.moveToMergeField(fieldName);
        insertHtml(injectValue, builder);
    }

    private void handleEasyreadNotes(String fieldName, FieldMergingArgs fieldMergingArgs) throws Exception {
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

            String html = htmlEasynoteForTerm(termName, value, defaultValue);

            // insert the relevant content into the document.
            DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
            builder.moveToMergeField(fieldName);
            insertHtml(html, builder);
        }
    }

    private void handleSignatureBlocks(String fieldName, FieldMergingArgs fieldMergingArgs) throws Exception {
        if ("guarentorSignatures".equals(fieldName)) {
            writeGuarentorSignaturesSection(fieldMergingArgs, tenancy);
            return;
        }

        if ("tenantSignatures".equals(fieldName)) {
            writeSignaturesSection(fieldMergingArgs,
                    tenancy.getTenants().stream()
                            .filter(tenant -> !FieldExtractorUtils.isEmpty(tenant))
                            .collect(Collectors.toList()), "Tenant");
            return;
        }

        if ("landlordSignatures".equals(fieldName)) {
            List<Person> landlords = tenancy.getLandlords().stream()
                    .filter(landlord -> !FieldExtractorUtils.isEmpty(landlord))
                    .collect(Collectors.toList());
            writeSignaturesSection(fieldMergingArgs, landlords, "Landlord");
            return;
        }
    }

    private void writeGuarentorSignaturesSection(FieldMergingArgs fieldMergingArgs, ModelTenancy tenancy) throws Exception {
        DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());

        List<Person> nonEmptyTenants = tenancy.getTenants()
                .stream()
                .filter(person -> !FieldExtractorUtils.isEmpty(person))
                .collect(toList());

        if (nonEmptyTenants.isEmpty()) {
            builder.moveToMergeField(fieldMergingArgs.getFieldName());
            writeGuarentorPlaceholderSignatureSection(builder, tenancy);
            return;
        }

        if (tenancy.getGuarantors().isEmpty()) {
            Section section = (Section) fieldMergingArgs.getField().getStart().getAncestor(Section.class);
            section.remove();
            return;
        }

        builder.moveToMergeField(fieldMergingArgs.getFieldName());

        for (int i = 0; i < tenancy.getGuarantors().size(); i++) {
            Guarantor guarantor = tenancy.getGuarantors().get(i);

            builder.getFont().setBold(true);
            builder.writeln("Guarantor " + (i + 1));
            builder.getFont().setBold(false);

            String tenantNames = guarantor.getTenantNames().stream().collect(joining(", "));
            builder.writeln("Name(s) of Tenant(s) for whom Guarantor " + (i + 1) + " will act as Guarantor:");
            writeIndented(builder, tenantNames);
            builder.writeln();

            builder.writeln("Full Name (Block  Capitals):");

            writeIndented(builder, toUpper(guarantor.getName()));
            builder.writeln();

            builder.writeln("Address:");
            writeIndented(builder, FieldExtractorUtils.addressFieldsMultipleLines(guarantor.getAddress()));
            builder.writeln();

            builder.writeln("Signature:");
            writeLines(builder, 2);
            builder.getParagraphFormat().getShading().setBackgroundPatternColor(Color.WHITE);
            builder.writeln();

            builder.writeln(DATE_LABEL);
            writeLines(builder, 2);
            builder.getParagraphFormat().getShading().setBackgroundPatternColor(Color.WHITE);
            builder.writeln();
        }
    }

    private void writeGuarentorPlaceholderSignatureSection(DocumentBuilder builder, ModelTenancy tenancy) {
        // fill in blanks for 5 guarentors
        for (int i = 1; i <= 5; i++) {
            ParagraphFormat paragraphFormat = builder.getParagraphFormat();
            paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
            builder.getFont().setBold(true);
            builder.writeln("Guarantor " + i);
            builder.getFont().setBold(false);
            builder.writeln("Name(s) of Tenant(s) for whom Guarantor " + i + " will act as Guarantor:\n");
            builder.writeln("Signature:\n");
            builder.writeln("Full Name (Block  Capitals):\n");
            builder.writeln("Address:\n\n\n");
            builder.writeln(DATE_LABEL);
            paragraphFormat.getShading().setBackgroundPatternColor(Color.WHITE);
            builder.writeln();
        }
    }

    private void writeSignaturesSection(FieldMergingArgs fieldMergingArgs, List<Person> people, String personType) throws Exception {

        DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
        builder.moveToMergeField(fieldMergingArgs.getFieldName());

        if (people.isEmpty()) {
            writePlaceholderSignatureSection(builder, personType);
            return;
        }

        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);

            builder.getFont().setBold(true);
            builder.writeln(personType + " " + (i + 1));
            builder.getFont().setBold(false);

            builder.writeln("Full Name (Block  Capitals):");
            writeIndented(builder, toUpper(person.getName()));
            builder.writeln();

            builder.writeln("Address:");
            writeIndented(builder, FieldExtractorUtils.addressFieldsMultipleLines(person.getAddress()));
            builder.writeln();

            builder.writeln("Signature:");
            writeLines(builder, 2);
            builder.getParagraphFormat().getShading().setBackgroundPatternColor(Color.WHITE);
            builder.writeln();

            builder.writeln(DATE_LABEL);
            writeLines(builder, 2);
            builder.getParagraphFormat().getShading().setBackgroundPatternColor(Color.WHITE);
            builder.writeln();
        }
    }

    private void writePlaceholderSignatureSection(DocumentBuilder builder, String personType) {

        // fill in blanks for 5 guarentors
        for (int i = 0; i < 5; i++) {
            ParagraphFormat paragraphFormat = builder.getParagraphFormat();
            paragraphFormat.getShading().setBackgroundPatternColor(Color.LIGHT_GRAY);
            builder.getFont().setBold(true);
            builder.writeln(personType + (i + 1));
            builder.getFont().setBold(false);
            builder.writeln("Full Name (Block  Capitals):\n");
            builder.writeln("Address:\n\n\n");
            builder.writeln("Signature:\n");
            builder.writeln(DATE_LABEL);
            paragraphFormat.getShading().setBackgroundPatternColor(Color.WHITE);
            builder.writeln();
        }
    }

    private String toUpper(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }

        return str.toUpperCase();
    }


    private boolean shouldRemoveSection(String fieldName, String fieldValue, FieldMergingArgs fieldMergingArgs) {
        if (fieldsToRemoveIfEmpty.contains(fieldName) && StringUtils.isEmpty(fieldValue)){
            return true;
        }

        // special case for alterations
        if (ALTERATIONS.equals(fieldName)
                && tenancy.getExcludedTerms().stream().anyMatch(t -> ALTERATIONS.equals(t))  ) {
            return true;
        }

        return false;
    }

    private String htmlEasynoteForTerm(String termName, String value, String defaultValue)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if ("utilities".equals(termName)) {
            // special case for utilities field.
            return easyreadNotesForUtilities(value, BeanUtils.getProperty(defaultNotes, termName));
        }

        if (!value.equals(defaultValue)) {
            // they changed the value, use the placeholder html.
            return EASYREAD_PLACEHOLDER_HTML;
        }

        // default to using the easytread notes for this field.
        return BeanUtils.getProperty(defaultNotes, termName);
    }

    private String formatAdditionalTerms(ModelTenancy tenancy) {
        if (tenancy.getAdditionalTerms().isEmpty()) {
            return "<p>n/a</p>";
        }

        return tenancy.getAdditionalTerms().stream().map(this::formatAdditionalTerm).collect(joining(NEWLINE));
    }

    private String formatAdditionalTerm(Term term) {
        return String.format("<div><strong>%s</strong></div><p>%s</p>", term.getTitle(), term.getContent());
    }

    private void provdePlacholder(FieldMergingArgs fieldMergingArgs) throws Exception {
        DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
        if (builder.moveToMergeField(fieldMergingArgs.getFieldName())) {
            placeholders.get(fieldMergingArgs.getFieldName()).accept(builder);
        }
    }

    /**
     * The utilities term contains a placeholder within []'s.
     */
    private static String easyreadNotesForUtilities(String utilitiesTerm, String defaultTerm) {
        String defaultUtilitiesTerm = TermsUtil.defaultOptionalTerms().getUtilities();
        String prefix = org.apache.commons.lang.StringUtils.substringBefore(defaultUtilitiesTerm, UTILITIES_LIST);
        String postfix = org.apache.commons.lang.StringUtils.substringAfter(defaultUtilitiesTerm, UTILITIES_LIST);
        if (utilitiesTerm.startsWith(prefix) && utilitiesTerm.endsWith(postfix)) {
            // inject the utilities from the term into the easyreadnotes
            String utilities = StringUtils.substringBetween(utilitiesTerm, prefix, postfix);
            return defaultTerm.replace("[]", utilities);
        }

        return EASYREAD_PLACEHOLDER_HTML;
    }

    private static final Set<String> fieldsToDeleteIfEmpty() {
        Set<String> fields = new HashSet<>();
        // add al of the optional sections.
        try {
            fields.addAll(BeanUtils.describe(new OptionalTerms()).keySet());
            fields.remove(ALTERATIONS);
            // other fields with sections
            addAll(fields,
                    "communicationsAgreementType",
                    "showHmoNotification",
                    "showHmoFields",
                    "showEmailParagraphs",
                    "showLettingAgentService");
            return fields;
        } catch (Exception e) {
            throw new InitialisationFailedException("Failed to extract optional section fields", e);
        }
    }

    private void insertHtml(String html, DocumentBuilder builder) throws Exception {
        // turn text para breaks into br's
        String htmlWithBreaks= html.replace("\n\n", "</br></br>");

        // ensure right font

        String htmlWithFont = String.format("<font face=\"arial\">%s</font>", htmlWithBreaks);
        builder.insertHtml(htmlWithFont);
    }
}