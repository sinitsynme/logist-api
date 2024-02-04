package ru.sinitsynme.logistapi.mapper;

import org.springframework.stereotype.Component;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;
import ru.sinitsynme.logistapi.rest.dto.UserUpdateDto;

@Component
public class UserMapper {

    public User fromSignUpDto(UserSignUpDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .build();
    }

    public User fromUpdateDto(UserUpdateDto dto) {
        return User.builder()
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .accountNonLocked(dto.isAccountNonLocked())
                .isAccountNonExpired(dto.isAccountNonExpired())
                .enabled(dto.isEnabled())
                .credentialsNonExpired(dto.isCredentialsNonExpired())
                .build();
    }
}
