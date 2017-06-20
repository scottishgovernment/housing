package scot.mygov.housing.modeltenancy;


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.modeltenancy.model.ModelTenancy;
import scot.mygov.housing.modeltenancy.validation.ObjectMother;

import java.util.Map;

/**
 * Created by z418868 on 20/06/2017.
 */
public class ModelTenancyFieldExtractorTest {

    private ObjectMother om = new ObjectMother();
    private ModelTenancyFieldExtractor sut = new ModelTenancyFieldExtractor();

    @Test
    public void canExtractTenants() {

        // ARRANGE
        ModelTenancy modelTenancy = om.anyTenancy();

        // ACT
        Map<String, Object> actual = sut.extractFields(modelTenancy);

        // ASSERT
        Assert.assertTrue(!StringUtils.isEmpty(actual.get("tenantNameAndAddresses").toString()));
        Assert.assertTrue(!StringUtils.isEmpty(actual.get("tenantEmails").toString()));
        Assert.assertTrue(!StringUtils.isEmpty(actual.get("tenantNameAndAddresses").toString()));
    }
}
