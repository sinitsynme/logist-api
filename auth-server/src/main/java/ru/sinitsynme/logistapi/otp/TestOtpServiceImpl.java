package ru.sinitsynme.logistapi.otp;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class TestOtpServiceImpl implements OtpService {
    private static final int TEST_OTP_VALUE = 123456;
    @Override
    public int generateAndCacheOtp(String username) {
        return TEST_OTP_VALUE;
    }

    @Override
    public boolean validateOtp(String username, int otp) {
        return TEST_OTP_VALUE == otp;
    }
}
