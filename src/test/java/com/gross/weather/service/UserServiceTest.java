package com.gross.weather.service;


import com.gross.weather.config.TestConfig;
import com.gross.weather.dto.UserDto;
import com.gross.weather.model.User;
import com.gross.weather.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class}) // это твой test config
@Transactional
@Rollback
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;



    @Test
    public void testRegisterUser_persistsToDatabase() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testUser");
        userDto.setPassword("testPassword");

        userService.register(userDto); // или createUser, если метод так называется

        em.flush(); // заставляем Hibernate записать в БД, иначе можно не увидеть эффект

        User fromDb = userRepository.findByLogin("testUser");
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getLogin()).isEqualTo("testUser");
    }
}
