package com.gross.weather.controllers;

import com.gross.weather.dto.LoginDto;
import com.gross.weather.model.Session;
import com.gross.weather.model.User;
import com.gross.weather.service.SessionService;
import com.gross.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class AuthController {
    private final UserService userService;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, SessionService sessionService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/sign-in")
    public String signIn(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "sign-in";
    }


    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("loginDto") @Valid LoginDto loginDto,
                         BindingResult bindingResult,
                          HttpServletResponse response) {



        User user = userService.findUserByLogin(loginDto.getLogin());
        if (user == null) {
            bindingResult.rejectValue("login", "error.login", "Такого пользователя не существует");
            return "sign-in";
        }
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
           bindingResult.rejectValue("password", "error.password", "Неверный логин или пароль");
           return "sign-in";
        }

        if (bindingResult.hasErrors()) {
            return "sign-in";
        }


            Session session = sessionService.saveSession(new Session(user.getId()));
            Cookie cookie = new Cookie("SESSION", session.getId().toString());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            response.addCookie(cookie);

            return "redirect:/";


        }


    }

