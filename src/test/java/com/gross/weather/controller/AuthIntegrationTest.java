package com.gross.weather.controller;

import com.gross.weather.config.TestConfig;
import com.gross.weather.dto.UserDto;
import com.gross.weather.service.SessionService;
import com.gross.weather.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class})
@Transactional

public class AuthIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void signIn_shouldCreateSessionAndSetCookie() throws Exception {
        // Arrange
        String login = "testlogin";
        String password = "testpassword";

        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setPassword(password);
        userService.register(userDto);
        System.out.println("============"+userService.findUserByLogin(login));
        // Act & Assert
        mockMvc.perform(post("/sign-in")
                        .param("login", login)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().httpOnly("SESSION", true));
    }

    @Test
    void signIn_shouldReturnSignInView_whenValidationFails() throws Exception {
        // Arrange
        String login = "nonexistentuser";
        String password = "wrongpassword";

        // Act & Assert
        mockMvc.perform(post("/sign-in")
                        .param("login", login)
                        .param("password", password))
                .andExpect(status().isOk()) // Страница возвращается с кодом 200
                .andExpect(view().name("sign-in")) // Возвращается именно view "sign-in"
                .andExpect(model().attributeHasFieldErrors("loginDto", "login")); // Ошибка в поле login
    }

    @Test
    void signIn_shouldReturnSignInView_whenPasswordIsIncorrect() throws Exception {
        // Arrange
        String login = "user1";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";

        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setPassword(correctPassword);
        userService.register(userDto);

        // Act & Assert
        mockMvc.perform(post("/sign-in")
                        .param("login", login)
                        .param("password", wrongPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"))
                .andExpect(model().attributeHasFieldErrors("loginDto", "password"));
    }

}
