package scot.mygov.housing.forms.noticetoleave;

import org.junit.Assert;
import org.junit.Test;
import scot.mygov.housing.forms.noticetoleave.model.NoticeToLeave;
import scot.mygov.housing.forms.noticetoleave.model.Reason;

import java.util.Map;

public class NoticeToLeaveFieldExtractorTest {

    scot.mygov.housing.forms.noticetoleave.NoticeToLeaveFieldExtractor sut = new scot.mygov.housing.forms.noticetoleave.NoticeToLeaveFieldExtractor();

    @Test
    public void allReasonsChecked() {

        // ARRANGE
        NoticeToLeave input = new NoticeToLeave();
        for (Reason r : Reason.values()) {
            input.getReasons().add(r.name());
        }

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        for (Reason r : Reason.values()) {
            Assert.assertEquals(output.get(r.name()), "X");
        }
    }

    @Test
    public void noReasonsChecked() {
        // ARRANGE
        NoticeToLeave input = new NoticeToLeave();

        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        for (Reason r : Reason.values()) {
            Assert.assertEquals(output.get(r.name()), "_");
        }
    }

    @Test
    public void oneReasonChecked() {
        NoticeToLeave input = new NoticeToLeave();
        input.getReasons().add(Reason.LANDLORD_FAMILY_MEMBER_TO_LIVE.name());


        // ACT
        Map<String, Object> output = sut.extractFields(input);

        // ASSERT
        for (Reason r : Reason.values()) {
            if (r == Reason.LANDLORD_FAMILY_MEMBER_TO_LIVE) {
                Assert.assertEquals(output.get(r.name()), "X");
            } else {
                Assert.assertEquals(output.get(r.name()), "_");
            }
        }
    }
}
