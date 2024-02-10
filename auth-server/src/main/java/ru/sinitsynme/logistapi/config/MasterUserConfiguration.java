package ru.sinitsynme.logistapi.config;

import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.sinitsynme.logistapi.config.properties.MasterUserProperties;
import ru.sinitsynme.logistapi.entity.BaseAuthorities;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;
import ru.sinitsynme.logistapi.service.UserService;

import java.util.List;

@Configuration
public class MasterUserConfiguration {
    private static final String MASTER = "MASTER";

    private static final Logger log = LoggerFactory.getLogger(MasterUserConfiguration.class);
    private final MasterUserProperties masterUserProperties;
    private final UserService userService;

    @Autowired
    public MasterUserConfiguration(MasterUserProperties masterUserProperties,
                                   UserService userService) {
        this.masterUserProperties = masterUserProperties;
        this.userService = userService;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {

            log.info("Starting to register master user");

            try {
                User user = userService.getUserByEmail(masterUserProperties.getEmail());
                if (user.getAuthorities().stream().anyMatch(it ->
                        !it.getName().equals(BaseAuthorities.ROLE_ADMIN.name()))
                ) {
                    log.error("User with given email exists in another role. Application is stopped");
                    throw new IllegalArgumentException("User with given email is registered. Application is stopped");
                }

                log.info("Master user with email provided in config was found. The password will be updated to provided");

                userService.updateMasterPasswordBySystem(user.getEmail(), masterUserProperties.getPassword().getBytes());
            } catch (UsernameNotFoundException | NotFoundException ex) {
                log.info("Master user with email provided in config was not found");

                log.info("Saving new master user");
                userService.saveUser(
                        buildMasterUserSignupDto(),
                        List.of(BaseAuthorities.ROLE_ADMIN.name())
                );

                log.info("New master user was saved");
            }
        };
    }

    private UserSignUpDto buildMasterUserSignupDto() {
        return UserSignUpDto.builder()
                .email(masterUserProperties.getEmail())
                .password(masterUserProperties.getPassword().getBytes())
                .firstName(MASTER)
                .lastName(MASTER)
                .middleName(MASTER)
                .build();
    }

}
