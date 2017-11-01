package scot.mygov.housing.forms.modeltenancy;


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.forms.modeltenancy.validation.ModelTenancyObjectMother;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModelTenancyFieldExtractorTest {

    private ModelTenancyObjectMother om = new ModelTenancyObjectMother();
    private ModelTenancyFieldExtractor sut = new ModelTenancyFieldExtractor();

    @Test
    public void canExtractTenants() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        String expected = om.validAddressFormatted();
        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals(expected, actual.get("tenantNamesAndAddresses"));
        assertTrue(!StringUtils.isEmpty(actual.get("tenantEmails").toString()));
    }

    @Test
    public void canExtractWithNoLettingAgent() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setLettingAgent(null);

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT - no fields start with "lettingAgent"
        assertEquals(" ", actual.get("communicationsAgreementEmail"));
    }



    @Test
    public void canExtractHMOFields() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setHmoProperty("true");
        modelTenancy.setHmoRegistrationExpiryDate(LocalDate.of(2010, 10, 01));
        modelTenancy.setHmo24ContactNumber("11111");

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("is", actual.get("hmoString"));
        assertEquals("11111", actual.get("hmoContactNumber"));
        assertEquals("01 October 2010", actual.get("hmoExpiryDate"));
    }

    @Test
    public void canExtractRentPressureZone() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setInRentPressureZone("true");

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("is", actual.get("rentPressureZoneString"));
    }

    @Test
    public void canExtractEmailComms() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setCommunicationsAgreement(CommunicationsAgreement.EMAIL.name());

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("X", actual.get("communicationsAgreementEmail"));
        assertEquals(" ", actual.get("communicationsAgreementHardcopy"));
    }

    @Test
    public void canExtractHardCopyComms() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setCommunicationsAgreement(CommunicationsAgreement.HARDCOPY.name());

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals(" ", actual.get("communicationsAgreementEmail"));
        assertEquals("X", actual.get("communicationsAgreementHardcopy"));
    }

    @Test
    public void canExtractAdvanceOrArearsForArrears() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setRentPayableInAdvance("false");

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("arrears", actual.get("advanceOrArrears"));

    }

    @Test
    public void canExtractAdvanceOrArearsForAdvance() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setRentPayableInAdvance("true");

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("advance", actual.get("advanceOrArrears"));

    }


    @Test
    public void canExtractWithEmptyCommsAgreement() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setCommunicationsAgreement("");

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        Assert.assertEquals(" ", actual.get("communicationsAgreementHardcopy"));
        Assert.assertEquals(" ", actual.get("communicationsAgreementEmail"));

    }
}
