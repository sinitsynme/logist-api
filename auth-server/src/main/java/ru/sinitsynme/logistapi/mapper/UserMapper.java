package ru.sinitsynme.logistapi.mapper;

import org.springframework.stereotype.Component;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.rest.dto.UserSignUpDto;

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
}
