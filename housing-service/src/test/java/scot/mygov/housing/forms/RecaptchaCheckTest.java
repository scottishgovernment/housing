package scot.mygov.housing.forms;

import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecaptchaCheckTest {

    @Test
    public void disabledRecaptchaPasses() {
        // ARRANGE
        RecaptchaCheck sut = new RecaptchaCheck(false, null, null);
        boolean expected = true;

        // ACT
        boolean actual = sut.verify(anyInput());

        // ASSERT
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void badRequestResponseFails() {
        // ARRANGE
        RecaptchaCheck sut = new RecaptchaCheck(true, badRequestTarget(), secretKey());
        boolean expected = false;

        // ACT
        boolean actual = sut.verify(anyInput());

        // ASSERT
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void serverErrorResponseFails() {
        // ARRANGE
        RecaptchaCheck sut = new RecaptchaCheck(true, badRequestTarget(), secretKey());
        boolean expected = false;

        // ACT
        boolean actual = sut.verify(anyInput());

        // ASSERT
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void okResponseWithSuccessFalseFails() {
        // ARRANGE
        RecaptchaResponse response = new RecaptchaResponse();
        response.setSuccess(false);
        RecaptchaCheck sut = new RecaptchaCheck(true, targetWithResponse(response), secretKey());
        boolean expected = false;

        // ACT
        boolean actual = sut.verify(anyInput());

        // ASSERT
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void okResponseWithSuccessTruePasses() {
        // ARRANGE
        RecaptchaResponse response = new RecaptchaResponse();
        response.setSuccess(true);
        RecaptchaCheck sut = new RecaptchaCheck(true, targetWithResponse(response), secretKey());
        boolean expected = true;

        // ACT
        boolean actual = sut.verify(anyInput());

        // ASSERT
        Assert.assertEquals(expected, actual);
    }

    private WebTarget badRequestTarget() {
        return target(Response.Status.Family.CLIENT_ERROR, null);
    }

    private WebTarget serverErrorTarget() {
        return target(Response.Status.Family.SERVER_ERROR, null);
    }

    private WebTarget targetWithResponse(RecaptchaResponse recaptchaResponse) {
        return target(Response.Status.Family.SUCCESSFUL, recaptchaResponse);
    }

    private Response ofFamily(Response.Status.Family family) {
        Response response = mock(Response.class);
        Response.StatusType statusType = mock(Response.StatusType.class);
        when(response.getStatusInfo()).thenReturn(statusType);
        when(statusType.getFamily()).thenReturn(family);
        return response;
    }

    private WebTarget target(Response.Status.Family family, RecaptchaResponse recaptchaResponse) {
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = ofFamily(family);
        when(target.request()).thenReturn(builder);
        when(builder.post(any())).thenReturn(response);
        when(response.readEntity(RecaptchaResponse.class)).thenReturn(recaptchaResponse);
        return target;
    }

    private String secretKey() {
        return "secret";
    }

    private String anyInput() {
        return "anyinput";
    }
}
