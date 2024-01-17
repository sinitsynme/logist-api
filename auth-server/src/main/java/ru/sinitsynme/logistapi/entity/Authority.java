package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Authority implements GrantedAuthority {
//    ROLE_SERVICE,
//    ROLE_HEAD_ADMIN
//    ROLE_ADMIN,
//    ROLE_OWNER,
//    ROLE_WORKER,
//    ROLE_DRIVER,
//    ROLE_CLIENT;

    @Id
    private String name;
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }
}
