package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Authority;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.mapper.UserMapper;
import ru.sinitsynme.logistapi.repository.UserRepository;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;

import javax.xml.transform.TransformerException;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.sinitsynme.logistapi.entity.BaseAuthorities.ROLE_CLIENT;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.USER_EXISTS_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.USER_EXISTS_TEMPLATE;

@Service
public class UserService {

    private final AuthorityService authorityService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(AuthorityService authorityService,
                       UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.authorityService = authorityService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUserAsClient(UserSignUpDto dto) {
        User user = userMapper.fromSignUpDto(dto);

        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException(
                    String.format(USER_EXISTS_TEMPLATE, dto.getEmail()),
                    null,
                    BAD_REQUEST,
                    USER_EXISTS_CODE,
                    ExceptionSeverity.WARN
            );
        }

        Authority authority = authorityService.getByName(ROLE_CLIENT.name());
        user.setAuthorities(Set.of(authority));

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()).getBytes());

        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User with email %s doesn't exist",
                                email
                        ))
                );
    }
}
