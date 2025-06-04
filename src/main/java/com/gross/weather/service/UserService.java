package com.gross.weather.service;

import com.gross.weather.dto.UserDto;
import com.gross.weather.model.User;
import com.gross.weather.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return saveUser(user);
    }


    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }



}
