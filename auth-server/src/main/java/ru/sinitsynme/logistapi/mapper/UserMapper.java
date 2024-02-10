package ru.sinitsynme.logistapi.mapper;

import org.springframework.stereotype.Component;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.rest.dto.user.UserDataDto;
import ru.sinitsynme.logistapi.rest.dto.user.UserSignUpDto;
import ru.sinitsynme.logistapi.rest.dto.user.UserUpdateDto;

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
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .build();
    }

    public UserDataDto toDataDto(User user) {
        return UserDataDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .blocked(!user.isAccountNonLocked())
                .disabled(!user.isEnabled())
                .build();
    }
}
