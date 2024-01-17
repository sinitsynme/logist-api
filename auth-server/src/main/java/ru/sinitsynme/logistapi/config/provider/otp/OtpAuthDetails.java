package ru.sinitsynme.logistapi.config.provider.otp;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Getter
public class OtpAuthDetails extends WebAuthenticationDetails {
    private static final String OTP = "otp";
    private String otp;
    public OtpAuthDetails(HttpServletRequest request) {
        super(request);
        otp = request.getParameter(OTP);
    }
}
