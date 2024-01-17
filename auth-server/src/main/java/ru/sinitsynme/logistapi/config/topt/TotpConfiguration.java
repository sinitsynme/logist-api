package ru.sinitsynme.logistapi.config.topt;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


@Configuration
public class TotpConfiguration {

    @Bean
    public Key key() throws NoSuchAlgorithmException {
        String algo = totpGenerator().getAlgorithm();
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(algo);
        int macLengthInBytes = Mac.getInstance(algo).getMacLength();
        keyGenerator.init(macLengthInBytes * 8);
        return keyGenerator.generateKey();
    }
    @Bean
    public TimeBasedOneTimePasswordGenerator totpGenerator() {
        return new TimeBasedOneTimePasswordGenerator();
    }
}
