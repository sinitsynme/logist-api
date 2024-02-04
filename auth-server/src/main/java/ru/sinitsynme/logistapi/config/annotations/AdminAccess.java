package ru.sinitsynme.logistapi.config.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEAD_ADMIN')")
public @interface AdminAccess {
}
