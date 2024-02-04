package ru.sinitsynme.logistapi.service;

import exception.service.BadRequestException;
import exception.service.ForbiddenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Authority;
import ru.sinitsynme.logistapi.entity.BaseAuthorities;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.mapper.UserMapper;
import ru.sinitsynme.logistapi.repository.UserRepository;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;
import ru.sinitsynme.logistapi.rest.dto.UserUpdateDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.FORBIDDEN_CODE;
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

    public void saveUser(UserSignUpDto dto, List<String> authorityNames) {
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

        user.setAuthorities(authorities);

        user.setPassword(passwordEncoder.encode(user.getPassword()).getBytes());
        userRepository.save(user);
    }

    public void updateUserPassword(String email, byte[] password) {
        User userFromDb = getUserByEmail(email);
        checkIfMasterAccountIsModified(userFromDb);

        userFromDb.setPassword(passwordEncoder.encode(new String(password)).getBytes());
        userRepository.save(userFromDb);
    }

    public void disableUser(String email) {
        changeEnabledUserStatus(email, false);
    }

    public void enableUser(String email) {
        changeEnabledUserStatus(email, true);
    }

    public void lockUserAccount(String email) {
        changeLockedUserStatus(email, false);
    }

    public void unlockUserAccount(String email) {
        changeLockedUserStatus(email, true);
    }

    public void updateUser(String email, UserUpdateDto updateDto, List<String> authorityNames) {
        User userFromDb = getUserByEmail(email);

        userFromDb.setPassword(updateDto.getPassword());
        userFromDb.setPhoneNumber(updateDto.getPhoneNumber());
        userFromDb.setFirstName(updateDto.getFirstName());
        userFromDb.setLastName(updateDto.getLastName());
        userFromDb.setMiddleName(updateDto.getMiddleName());
        userFromDb.setEnabled(updateDto.isEnabled());
        userFromDb.setCredentialsNonExpired(updateDto.isCredentialsNonExpired());
        userFromDb.setAccountNonLocked(updateDto.isAccountNonLocked());
        userFromDb.setAccountNonExpired(updateDto.isAccountNonExpired());

        Set<Authority> authorities = authorityNames.stream()
                .map(authorityService::getByName)
                .collect(Collectors.toSet());

        userFromDb.setAuthorities(authorities);

        userRepository.save(userFromDb);
    }

    public Page<User> getUsersByAuthority(String authorityName, Pageable pageable) {
        Authority authority = authorityService.getByName(authorityName);
        return userRepository.findByAuthoritiesIn(Set.of(authority), pageable);
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

    public Set<Authority> getUserAuthorities(String email) {
        User user = getUserByEmail(email);
        return user.getAuthorities();
    }

    public Set<String> getUserAuthoritiesNames(String email) {
        return getUserAuthorities(email)
                .stream()
                .map(Authority::getName)
                .collect(Collectors.toSet());
    }

    private void changeEnabledUserStatus(String email, boolean status) {
        User userFromDb = getUserByEmail(email);
        checkIfMasterAccountIsModified(userFromDb);

        userFromDb.setEnabled(status);
        userRepository.save(userFromDb);
    }

    private void changeLockedUserStatus(String email, boolean status) {
        User userFromDb = getUserByEmail(email);
        checkIfMasterAccountIsModified(userFromDb);

        userFromDb.setAccountNonLocked(status);
        userRepository.save(userFromDb);
    }

    private void checkIfMasterAccountIsModified(User modifiedUser) {
        if (modifiedUser.getAuthorities()
                .stream()
                .noneMatch(it -> it.getAuthority().equals(BaseAuthorities.ROLE_HEAD_ADMIN.name()))) {
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
                .anyMatch(it -> it.getAuthority().equals(BaseAuthorities.ROLE_HEAD_ADMIN.name()));
    }

    /**
     WARN! Use only in configs when system is performing changes to master users!
     */
    public void updateMasterPasswordBySystem(String email, byte[] password) {
        User userFromDb = getUserByEmail(email);

        userFromDb.setPassword(passwordEncoder.encode(new String(password)).getBytes());
        userRepository.save(userFromDb);
    }
}
