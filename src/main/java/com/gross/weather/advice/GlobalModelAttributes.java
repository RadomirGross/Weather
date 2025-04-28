package com.gross.weather.advice;

import com.gross.weather.exceptions.UserNotAuthenticatedException;
import com.gross.weather.model.Session;
import com.gross.weather.model.User;
import com.gross.weather.service.SessionService;
import com.gross.weather.service.UserService;
import com.gross.weather.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ControllerAdvice
public class GlobalModelAttributes {
    private final SessionService sessionService;
    private final UserService userService;

    public GlobalModelAttributes(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User addUserToModel(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/sign-in")) {
            return null;
        }
        Optional<UUID> token = CookieUtils.extractUuidFromCookie(request, "SESSION");
        if (token.isPresent()) {

                Session session = sessionService.findSessionById(token.get());
                if (session != null) {
                    return userService.findUserById(session.getUserId());
                }

        }
        throw new UserNotAuthenticatedException("Необходима авторизация");

    }
}