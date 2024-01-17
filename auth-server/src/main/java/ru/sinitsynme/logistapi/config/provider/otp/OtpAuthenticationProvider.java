package ru.sinitsynme.logistapi.config.provider.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.otp.OtpService;
import ru.sinitsynme.logistapi.repository.UserRepository;

public class OtpAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserRepository userRepository;
    private final OtpService otpService;

    @Autowired
    public OtpAuthenticationProvider(UserRepository userRepository, OtpService otpService) {
        this.userRepository = userRepository;
        this.otpService = otpService;
    }

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String otp = ((OtpAuthDetails) auth.getDetails()).getOtp();

        User user = userRepository.findByEmail(auth.getName()).orElseThrow(
                () -> new BadCredentialsException("Invalid username or password"));

        otpService.validateOtp(user.getUsername(), Integer.parseInt(otp));

        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
