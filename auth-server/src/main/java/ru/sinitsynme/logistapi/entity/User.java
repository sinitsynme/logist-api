package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String email;
    private String phoneNumber;
    private byte[] password;
    private String firstName;
    private String middleName;
    private String lastName;
    private Set<LogapiAuthority> authorities;
    private boolean isAccountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return new String(password);
    }
}
