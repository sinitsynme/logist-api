package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.ForbiddenException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Authority;
import ru.sinitsynme.logistapi.entity.BaseAuthorities;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.mapper.UserMapper;
import ru.sinitsynme.logistapi.repository.UserRepository;
import ru.sinitsynme.logistapi.rest.dto.user.UserDataDto;
import ru.sinitsynme.logistapi.rest.dto.user.UserSignUpDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.*;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.*;

@Service
public class UserService {

    private final AuthorityService authorityService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(AuthorityService authorityService,
                       UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.authorityService = authorityService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(UserSignUpDto dto, List<String> authorityNames) {
        User user = userMapper.fromSignUpDto(dto);

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException(
                    String.format(USER_EXISTS_TEMPLATE, dto.getEmail()),
                    null,
                    BAD_REQUEST,
                    USER_EXISTS_CODE,
                    WARN
            );
        }

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        Set<Authority> authorities = authorityNames.stream()
                .map(authorityService::getByName)
                .collect(Collectors.toSet());

        if (authorities.stream()
                .noneMatch(it -> it
                        .getAuthority()
                        .equals(BaseAuthorities.ROLE_CLIENT.name()))) {
            authorities.add(authorityService.getByName(BaseAuthorities.ROLE_CLIENT.name()));
        }

        user.setAuthorities(authorities);

        user.setPassword(passwordEncoder.encode(user.getPassword()).getBytes());
        return userRepository.save(user);
    }

    public void updateUserPassword(UUID id, byte[] password) {
        User userFromDb = getUserById(id);
        checkIfMasterAccountIsModified(userFromDb);

        userFromDb.setPassword(passwordEncoder.encode(new String(password)).getBytes());
        userRepository.save(userFromDb);
    }

    public void disableUser(UUID id) {
        changeEnabledUserStatus(id, false);
    }

    public void enableUser(UUID id) {
        changeEnabledUserStatus(id, true);
    }

    public void lockUserAccount(UUID id) {
        changeLockedUserStatus(id, false);
    }

    public void unlockUserAccount(UUID id) {
        changeLockedUserStatus(id, true);
    }

    public void updateUserData(UUID id, UserDataDto updateDto) {
        User userFromDb = getUserById(id);

        userFromDb.setPhoneNumber(updateDto.getPhoneNumber());
        userFromDb.setFirstName(updateDto.getFirstName());
        userFromDb.setLastName(updateDto.getLastName());
        userFromDb.setMiddleName(updateDto.getMiddleName());

        userRepository.save(userFromDb);
    }

    public Page<User> getUsersByAuthority(Authority authority, Pageable pageable) {
        return userRepository.findByAuthoritiesIn(Set.of(authority), pageable);
    }

    public Page<User> getUsers(Pageable pageable) {
        try {
            return userRepository.findAll(pageable);
        } catch (Exception ex) {
            throw new BadRequestException(
                    String.format("Something is wrong with pageable: %s", ex.getMessage()),
                    ex,
                    HttpStatus.BAD_REQUEST,
                    "-1",
                    ExceptionSeverity.WARN
            );
        }
    }

    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(USER_EMAIL_NOT_FOUND_TEMPLATE, email),
                                null,
                                HttpStatus.NOT_FOUND,
                                USER_NOT_FOUND_CODE,
                                WARN
                        )
                );
    }

    public User getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(USER_ID_NOT_FOUND_TEMPLATE, id),
                                null,
                                HttpStatus.NOT_FOUND,
                                USER_NOT_FOUND_CODE,
                                WARN
                        )
                );
    }

    public Set<Authority> getUserAuthorities(UUID id) {
        User user = getUserById(id);
        return user.getAuthorities();
    }

    public Set<String> getUserAuthoritiesNames(UUID id) {
        return getUserAuthorities(id)
                .stream()
                .map(Authority::getName)
                .collect(Collectors.toSet());
    }

    public void grantAuthorityToUser(UUID id, String authorityName) {
        User user = getUserById(id);
        Authority authority = authorityService.getByName(authorityName);

        if (user.getAuthorities().contains(authority)) {
            throw new BadRequestException(
                    String.format(USER_ALREADY_HAS_ROLE_TEMPLATE, id),
                    null,
                    BAD_REQUEST,
                    USER_ALREADY_HAS_AUTHORITY_CODE,
                    WARN
            );
        }

        user.getAuthorities().add(authority);

        userRepository.save(user);
        log.info("User {} was granted with role {}", user.getId(), authorityName);
    }

    public void revokeAuthorityFromUser(UUID id, String authorityName) {
        if (authorityName.equals(BaseAuthorities.ROLE_CLIENT.name())) {
            throw new BadRequestException(
                    String.format(CLIENT_ROLE_IS_TRIED_TO_BE_REVOKED_TEMPLATE, id),
                    null,
                    BAD_REQUEST,
                    CLIENT_ROLE_IS_TRIED_TO_BE_REVOKED_CODE,
                    WARN
            );
        }

        User user = getUserById(id);
        Authority authority = authorityService.getByName(authorityName);

        if (!user.getAuthorities().contains(authority)) {
            throw new BadRequestException(
                    String.format(USER_DOESNT_HAVE_ROLE_TEMPLATE, id, authorityName),
                    null,
                    BAD_REQUEST,
                    USER_DOESNT_HAVE_ROLE_CODE,
                    WARN
            );
        }

        user.getAuthorities().remove(authority);

        userRepository.save(user);
        log.info("Role {} was revoked from user {}", authorityName, user.getId());
    }

    private void changeEnabledUserStatus(UUID id, boolean status) {
        User userFromDb = getUserById(id);
        checkIfMasterAccountIsModified(userFromDb);

        if (userFromDb.isEnabled() == status) {
            throw new BadRequestException(
                    String.format(USER_ALREADY_DISABLED_TEMPLATE, id),
                    null,
                    BAD_REQUEST,
                    USER_ALREADY_DISABLED_CODE,
                    WARN
            );
        }

        userFromDb.setEnabled(status);
        userRepository.save(userFromDb);
    }

    private void changeLockedUserStatus(UUID id, boolean status) {
        User userFromDb = getUserById(id);
        checkIfMasterAccountIsModified(userFromDb);

        if (userFromDb.isAccountNonLocked() == status) {
            throw new BadRequestException(
                    String.format(USER_ALREADY_BLOCKED_TEMPLATE, id),
                    null,
                    BAD_REQUEST,
                    USER_ALREADY_BLOCKED_CODE,
                    WARN
            );
        }

        userFromDb.setAccountNonLocked(status);
        userRepository.save(userFromDb);
    }

    private void checkIfMasterAccountIsModified(User modifiedUser) {
        if (modifiedUser.getAuthorities()
                .stream()
                .noneMatch(it -> it.getAuthority().equals(BaseAuthorities.ROLE_ADMIN.name()))) {
            return;
        }
        checkMasterAuthority();
    }

    public void checkMasterAuthority() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!isMasterAction(principal)) {
            throw new ForbiddenException(
                    "This action can be done only by MASTER account",
                    null,
                    FORBIDDEN,
                    FORBIDDEN_CODE,
                    WARN
            );
        }
    }

    private boolean isMasterAction(UserDetails principal) {
        return principal
                .getAuthorities()
                .stream()
                .anyMatch(it -> it.getAuthority().equals(BaseAuthorities.ROLE_ADMIN.name()));
    }

    /**
     * WARN! Use only in configs when system is performing changes to master users!
     */
    public void updateMasterPasswordBySystem(String email, byte[] password) {
        User userFromDb = getUserByEmail(email);

        userFromDb.setPassword(passwordEncoder.encode(new String(password)).getBytes());
        userRepository.save(userFromDb);
    }
}
