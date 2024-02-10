package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;


@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Authority implements GrantedAuthority {

    @Id
    private String name;
    private String description;
    private boolean basic;

    @Override
    public String getAuthority() {
        return name;
    }
}
