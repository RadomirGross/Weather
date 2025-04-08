package com.gross.weather.controllers;

import com.gross.weather.exceptions.InvalidSessionTokenException;
import com.gross.weather.model.Session;
import com.gross.weather.service.SessionService;
import com.gross.weather.service.UserService;
import com.gross.weather.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
public class WeatherController {
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public WeatherController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }


    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {

        Optional<UUID> token = CookieUtils.extractCookieId(request, "SESSION");
        if (token.isPresent()) {
            try {
                Session session = sessionService.findSessionById(token.get());
                if (session != null) {
                    System.out.println("session found!!!!!!!!!!!!!!!!!!!!!!!!!");
                    model.addAttribute("user", userService.findUserById(session.getUserId()));
                    return "index";
                }
            } catch (IllegalArgumentException e) {
                throw new InvalidSessionTokenException(token.get().toString());
            }
        }

            return "redirect:/sign-up";
        }
    }
