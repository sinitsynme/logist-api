package ru.sinitsynme.logistapi.service;

import exception.service.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.BaseAuthorities;
import ru.sinitsynme.logistapi.entity.User;

import java.util.UUID;

import static exception.ExceptionSeverity.WARN;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.FORBIDDEN_CODE;

@Service
public class PrincipalService {

    private final UserService userService;

    @Autowired
    public PrincipalService(UserService userService) {
        this.userService = userService;
    }

    public void assertPrincipalAction(UUID id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);

        if (!user.getId().equals(id)) {
            throw new ForbiddenException(
                    "This action is forbidden",
                    null,
                    FORBIDDEN,
                    FORBIDDEN_CODE,
                    WARN
            );
        }
    }

    public void checkMasterAuthority() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);

        if (!isMasterAction(user)) {
            throw new ForbiddenException(
                    "This action can be done only by MASTER account",
                    null,
                    FORBIDDEN,
                    FORBIDDEN_CODE,
                    WARN
            );
        }
    }


    private boolean isMasterAction(User user) {
        return user
                .getAuthorities()
                .stream()
                .anyMatch(it -> it.getAuthority().equals(BaseAuthorities.ROLE_ADMIN.name()));
    }
}
