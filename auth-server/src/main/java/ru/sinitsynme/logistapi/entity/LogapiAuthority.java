package ru.sinitsynme.logistapi.entity;

import org.springframework.security.core.GrantedAuthority;

public enum LogapiAuthority implements GrantedAuthority {
    ROLE_SERVICE,
    ROLE_ADMIN,
    ROLE_OWNER,
    ROLE_WORKER,
    ROLE_DRIVER,
    ROLE_CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
