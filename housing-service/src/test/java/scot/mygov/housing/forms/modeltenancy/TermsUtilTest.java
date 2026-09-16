package scot.mygov.housing.forms.modeltenancy;

import org.junit.After;
import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.OptionalTerms;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TermsUtilTest {

    @After
    public void resetClock() {
        TermsUtil.clock = LocalDate::now;
    }

    @Test
    public void usesOldWordingBeforeLegislationChangeDate2026() {
        TermsUtil.clock = () -> LocalDate.of(2026, 10, 5);

        OptionalTerms terms = TermsUtil.defaultOptionalTerms();

        assertTrue(terms.getContentsAndConditions().contains(
                "either 1) to tell the Landlord of any discrepancies in writing"));
    }

    @Test
    public void usesNewWordingOnLegislationChangeDate2026() {
        TermsUtil.clock = () -> LocalDate.of(2026, 10, 6);

        OptionalTerms terms = TermsUtil.defaultOptionalTerms();

        assertTrue(terms.getContentsAndConditions().contains(
                "either:  tell the Landlord of any discrepancies in writing"));
    }

    @Test
    public void usesNewWordingAfterLegislationChangeDate2026() {
        TermsUtil.clock = () -> LocalDate.of(2027, 1, 1);

        OptionalTerms terms = TermsUtil.defaultOptionalTerms();

        assertTrue(terms.getContentsAndConditions().contains(
                "either:  tell the Landlord of any discrepancies in writing"));
    }

    @Test
    public void termsWithNoDatedVariantAreUnaffectedByDate() {
        TermsUtil.clock = () -> LocalDate.of(2026, 10, 5);
        String before = TermsUtil.defaultOptionalTerms().getUtilities();

        TermsUtil.clock = () -> LocalDate.of(2027, 1, 1);
        String after = TermsUtil.defaultOptionalTerms().getUtilities();

        assertEquals(before, after);
    }

    @Test
    public void contentsAndConditionsWordingDiffersEitherSideOfLegislationChangeDate2026() {
        TermsUtil.clock = () -> LocalDate.of(2026, 10, 5);
        String before = TermsUtil.defaultOptionalTerms().getContentsAndConditions();

        TermsUtil.clock = () -> LocalDate.of(2026, 10, 6);
        String after = TermsUtil.defaultOptionalTerms().getContentsAndConditions();

        assertNotEquals(before, after);
    }
}
