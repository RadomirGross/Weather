package com.gross.weather.interceptor;

import com.gross.weather.model.Session;
import com.gross.weather.model.User;
import com.gross.weather.service.SessionService;
import com.gross.weather.service.UserService;
import com.gross.weather.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;
import java.util.UUID;


public class AuthInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;
    private final UserService userService;

    public AuthInterceptor(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/sign-in") || uri.startsWith("/sign-up")) {
            return true;
        }

        Optional<UUID> token = CookieUtils.extractUuidFromCookie(request, "SESSION");
        if (token.isPresent()) {
            Session session = sessionService.findSessionById(token.get());
            if (session != null ) {
                User user = userService.findUserById(session.getUserId());
                if (user != null) {
                    request.setAttribute("user", user);
                    return true;
                }
            }
        }

        request.getSession().setAttribute("error", "Необходима авторизация");
        response.sendRedirect("/sign-in");
        return false;
    }

}
