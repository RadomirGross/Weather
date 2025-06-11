package com.gross.weather.service;

import com.gross.weather.config.TestConfig;
import com.gross.weather.exceptions.LoginAlreadyExistsException;
import com.gross.weather.testcontainer.PostgresContainerHolder;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@Rollback
@WebAppConfiguration
public class UserServiceTest {

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PostgresContainerHolder.POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PostgresContainerHolder.POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PostgresContainerHolder.POSTGRES_CONTAINER::getPassword);
    }

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

        userService.register(userDto);
        em.flush();

        User fromDb = userRepository.findByLogin("testUser");
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getLogin()).isEqualTo("testUser");
    }

    @Test
    public void testRegisterUser_LoginAlreadyExistsException() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testUser");
        userDto.setPassword("testPassword");

        userService.register(userDto);
        em.flush();

        assertThrows(LoginAlreadyExistsException.class, ()
                -> userService.register(userDto));
    }


}
