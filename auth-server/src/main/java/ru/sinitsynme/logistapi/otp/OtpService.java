package ru.sinitsynme.logistapi.otp;

import java.security.InvalidKeyException;

public interface OtpService {
    int generateAndCacheOtp(String username) throws InvalidKeyException;
    boolean validateOtp(String username, int otp);
}
