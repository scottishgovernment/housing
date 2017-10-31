package scot.mygov.housing.forms.modeltenancy;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.Section;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import scot.mygov.housing.forms.InitialisationFailedException;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;

import java.util.HashSet;
import java.util.Set;

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

    public ModelTenancyMergingCallback(ModelTenancy tenancy) {
        this.tenancy = tenancy;
    }

    @Override
    public void fieldMerging(FieldMergingArgs fieldMergingArgs) throws Exception {

        String fieldValue = fieldMergingArgs.getFieldValue() == null ?
                null : fieldMergingArgs.getFieldValue().toString();

        String fieldName = fieldMergingArgs.getFieldName();

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
            } else
            {
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
            builder.insertHtml(html);
        }
    }


    /**
     * The utilities term contains a placeholder within []'s.
     *
     * In order to decide if we oinclude the easyreadnotes for
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
            addAll(fields, "hmoString", "rentPressureZoneString", "communicationsAgreementType");
            return fields;
        } catch (Exception e) {
            throw new InitialisationFailedException("Failed to extract optional section fields", e);
        }
    }
}