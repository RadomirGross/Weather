package com.gross.weather.service;

import com.gross.weather.model.User;
import com.gross.weather.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> findAllUsers() {
        return usersRepository.findAll();
    }

    public User findUserById(Integer id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        return usersRepository.save(user);
    }

    @Transactional
    public void updateUser(int id,User user) {
        user.setId(id);
        usersRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Integer id) {
        usersRepository.deleteById(id);
    }

}
