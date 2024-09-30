package scot.mygov.housing;

import org.junit.Test;
import software.amazon.awssdk.services.s3.S3Client;

import static org.assertj.core.api.Assertions.assertThat;

public class HousingModuleTest {

    @Test
    public void configuresS3ClientIfRegionSet() {
        HousingModule module = new HousingModule();
        HousingConfiguration configuration = new HousingConfiguration();
        configuration.setRegion("eu-west-1");
        S3Client s3 = module.s3(configuration);
        assertThat(s3).isNotNull();
    }

    @Test
    public void configuresS3ClientIfRegionIsNotSet() {
        HousingModule module = new HousingModule();
        HousingConfiguration configuration = new HousingConfiguration();
        S3Client s3 = module.s3(configuration);
        assertThat(s3).isNull();
    }

}
