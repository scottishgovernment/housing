package scot.mygov.housing;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Test;
import scot.mygov.config.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

public class HousingModuleTest {

    @Test
    public void configuresS3ClientIfRegionSet() {
        HousingModule module = new HousingModule();
        HousingConfiguration configuration = new HousingConfiguration();
        configuration.setRegion("eu-west-1");
        AmazonS3 s3 = module.s3(configuration);
        assertThat(s3).isNotNull();
    }

    @Test
    public void configuresS3ClientIfRegionIsNotSet() {
        HousingModule module = new HousingModule();
        HousingConfiguration configuration = new HousingConfiguration();
        AmazonS3 s3 = module.s3(configuration);
        assertThat(s3).isNull();
    }

}
