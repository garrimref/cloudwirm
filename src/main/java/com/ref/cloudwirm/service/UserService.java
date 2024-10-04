package com.ref.cloudwirm.service;

import com.ref.cloudwirm.domain.Role;
import com.ref.cloudwirm.domain.User;
import com.ref.cloudwirm.dto.UserDto;
import com.ref.cloudwirm.exception.UserAlreadyExistException;
import com.ref.cloudwirm.repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new UserAlreadyExistException("There is an account with that username: "
                    + userDto.getUsername());
        }
        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                Set.of(Role.USER)
        );
        userRepository.save(user);
    }
}
