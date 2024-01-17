package ru.sinitsynme.logistapi.otp;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import exception.ExceptionSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.exception.OtpIsNotFoundOrExpiredException;
import ru.sinitsynme.logistapi.exception.OtpIsWrongException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.time.Clock;

import static java.util.concurrent.TimeUnit.MINUTES;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.OTP_IS_WRONG_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.OTP_NOT_FOUND_OR_EXPIRED_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.OTP_IS_WRONG_TEMPLATE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.OTP_NOT_FOUND_OR_EXPIRED_TEMPLATE;

@Service
@Profile("!dev")
public class OtpServiceImpl implements OtpService {
    private final LoadingCache<String, Integer> loadingCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(5, MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public Integer load(String key) throws Exception {
                    return 0;
                }
            });

    private final Key totpKey;
    private final Clock clock;
    private final TimeBasedOneTimePasswordGenerator totpGenerator;

    @Autowired
    public OtpServiceImpl(Key totpKey, Clock clock, TimeBasedOneTimePasswordGenerator totpGenerator) {
        this.totpKey = totpKey;
        this.clock = clock;
        this.totpGenerator = totpGenerator;
    }

    public int generateAndCacheOtp(String username) throws InvalidKeyException {
        int otp = totpGenerator.generateOneTimePassword(totpKey, clock.instant());
        loadingCache.put(username, otp);
        return otp;
    }

    public boolean validateOtp(String username, int otp) {
        Integer cachedOtp = loadingCache.getIfPresent(username);
        if (cachedOtp == null) {
            throw new OtpIsNotFoundOrExpiredException(
                    String.format(OTP_NOT_FOUND_OR_EXPIRED_TEMPLATE, username),
                    null,
                    HttpStatus.UNAUTHORIZED,
                    OTP_NOT_FOUND_OR_EXPIRED_CODE,
                    ExceptionSeverity.WARN);
        }
        if (cachedOtp != otp) {
            throw new OtpIsWrongException(
                    String.format(OTP_IS_WRONG_TEMPLATE, username),
                    null,
                    HttpStatus.UNAUTHORIZED,
                    OTP_IS_WRONG_CODE,
                    ExceptionSeverity.WARN);
        }
        loadingCache.invalidate(username);
        return true;
    }

}
