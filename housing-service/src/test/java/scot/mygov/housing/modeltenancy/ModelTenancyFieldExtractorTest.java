package scot.mygov.housing.modeltenancy;


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.CommunicationsAgreement;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModelTenancyFieldExtractorTest {

    private ObjectMother om = new ObjectMother();
    private ModelTenancyFieldExtractor sut = new ModelTenancyFieldExtractor();

    @Test
    public void canExtractTenants() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        String expected = om.validAdressFormatted();
        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals(expected, actual.get("tenantNameAndAddresses"));
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
        assertEquals("_", actual.get("communicationsAgreementEmail"));

        assertEquals(0, actual.keySet().stream().filter(key -> key.startsWith("lettingAgent")).count());
    }

    @Test
    public void canExtractGuarentorSignatureblock() {

        // ARRANGE
        ModelTenancy modelTenancy = om.tenancyWithGuarentors();
        String expected = "(1) name, 21 Some random street, Randomtown, Midlothian, EH104AX";

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals(expected, actual.get("tenantNameAndAddresses"));
    }


    @Test
    public void canExtractHMOFields() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setHmoProperty(true);
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
        modelTenancy.setInRentPressureZone(true);

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
        assertEquals("_", actual.get("communicationsAgreementHardcopy"));
        assertEquals("email", actual.get("communicationsAgreementType"));
    }

    @Test
    public void canExtractHardCopyComms() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setCommunicationsAgreement(CommunicationsAgreement.HARDCOPY.name());

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("_", actual.get("communicationsAgreementEmail"));
        assertEquals("X", actual.get("communicationsAgreementHardcopy"));
        assertEquals("", actual.get("communicationsAgreementType"));
    }

    @Test
    public void canExtractAdvanceOrArearsForArrears() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setRentPayableInAdvance(false);

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("arrears", actual.get("advanceOrArrears"));

    }

    @Test
    public void canExtractAdvanceOrArearsForAdvance() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();
        modelTenancy.setRentPayableInAdvance(true);

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        assertEquals("advance", actual.get("advanceOrArrears"));

    }

}
