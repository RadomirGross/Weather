package com.gross.weather.controllers;

import com.gross.weather.model.Session;
import com.gross.weather.service.SessionService;
import com.gross.weather.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
public class LogoutController {

    private final SessionService sessionService;
    private final Logger logger;

    @Autowired
    public LogoutController(SessionService sessionService) {
        this.sessionService = sessionService;
        this.logger = LoggerFactory.getLogger(LogoutController.class);
    }

    @GetMapping("/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        UUID token;
        Optional<Cookie> cookieOptional = CookieUtils.extractCookieFromRequest(request, "SESSION");
        Cookie cookie;

        if (cookieOptional.isPresent()) {
            cookie = cookieOptional.get();
            try {
                token = UUID.fromString(cookie.getValue());
                CookieUtils.clearCookie(cookie, response);
                Session session = sessionService.findSessionById(token);
                if (session != null) {
                    sessionService.deleteSession(session);
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Ошибка при извлечении UUID из cookie: {}", cookie.getValue(), e);
            }
        }
        return "redirect:/sign-in";
    }
}
