package com.gross.weather.interceptor;

import com.gross.weather.config.TestConfig;
import com.gross.weather.dto.UserDto;
import com.gross.weather.model.Session;
import com.gross.weather.model.User;
import com.gross.weather.repositories.SessionRepository;
import com.gross.weather.service.SessionService;
import com.gross.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class AuthInterceptorTest {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionRepository sessionRepo;

    @Test
    void preHandle_validSession_allowsRequest() throws Exception {
        // 1. регистрируем пользователя
        User u = userService.register(new UserDto("test", "testpass", "testpass"));

        // 2. создаём сессию
        Session s = sessionService.saveSession(new Session(u.getId()));

        // 3. готовим mock‑request c cookie
        MockHttpServletRequest req  = new MockHttpServletRequest("GET", "/");
        req.setCookies(new Cookie("SESSION", s.getId().toString()));
        MockHttpServletResponse res = new MockHttpServletResponse();

        AuthInterceptor interceptor = new AuthInterceptor(sessionService, userService);

        boolean allowed = interceptor.preHandle(req, res, new Object());

        assertThat(allowed).isTrue();
        assertThat(req.getAttribute("user")).isEqualTo(u);
    }
}
