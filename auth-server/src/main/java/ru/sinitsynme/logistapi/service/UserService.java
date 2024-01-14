package ru.sinitsynme.logistapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
