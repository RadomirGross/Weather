package com.gross.weather.controllers;

import com.gross.weather.model.Session;
import com.gross.weather.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class LogOutController {

    private final SessionService sessionService;

    @Autowired
    public LogOutController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        UUID token = null;

        if (cookies != null)
            for (Cookie cookie : cookies) {
                if ("SESSION".equals(cookie.getName())) {
                    try {
                        token = UUID.fromString(cookie.getValue());
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);


                    } catch (IllegalArgumentException e) {

                    }
                    break;
                }
            }

        if (token != null) {
            Session session = sessionService.findSessionById(token);
            if (session != null)
                sessionService.deleteSession(session);
        }

        return "redirect:/sign-in";


    }
}
