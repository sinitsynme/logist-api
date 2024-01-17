package ru.sinitsynme.logistapi.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.sinitsynme.logistapi.config.provider.otp.OtpAuthDetailsSource;
import ru.sinitsynme.logistapi.config.provider.otp.OtpAuthenticationProvider;
import ru.sinitsynme.logistapi.otp.OtpService;
import ru.sinitsynme.logistapi.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final UserDetailsService userDetailsService;
    private final BCryptProperties bCryptProperties;
    private final OtpAuthDetailsSource otpAuthDetailsSource;

    @Autowired
    public SecurityConfiguration(UserRepository userRepository,
                                 OtpService otpService,
                                 UserDetailsService userDetailsService,
                                 BCryptProperties bCryptProperties,
                                 OtpAuthDetailsSource otpAuthDetailsSource) {
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.userDetailsService = userDetailsService;
        this.bCryptProperties = bCryptProperties;
        this.otpAuthDetailsSource = otpAuthDetailsSource;
    }

    //FIXME
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(login -> login.authenticationDetailsSource(otpAuthDetailsSource));
//        http.addFilter();
//        http.addFilterAfter();
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("/authority**").authenticated()
                        .requestMatchers("/**").permitAll()
//                .requestMatchers("/authority").hasAnyAuthority("ROLE_ADMIN", "ROLE_HEAD_ADMIN")
        );
        http.authenticationProvider(authenticationProvider());
        http.exceptionHandling(handling -> handling.authenticationEntryPoint((
                (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                authException.getMessage()))
        ));
//        http.httpBasic(withDefaults());
//        http.sessionManagement(mng -> mng.sessionCreationPolicy(STATELESS));

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bcryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(bCryptProperties.getEncryptionRounds());
    }


}
