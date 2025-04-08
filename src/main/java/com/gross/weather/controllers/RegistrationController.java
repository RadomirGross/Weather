package com.gross.weather.controllers;

import com.gross.weather.model.User;
import com.gross.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
@Controller
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUp() {
        System.out.println("Отработал контроллер sign-up"); // Проверяем, что метод вызывается
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@RequestParam("username") String username, @RequestParam("password") String password,
                         @RequestParam("repeat-password") String repeatPassword, Model model) {

        List<String> errors = new ArrayList<>();
        if (userService.findUserByLogin(username) != null) {
            errors.add("login already exists");
            model.addAttribute("errors", errors);
            return "sign-up";
        }

        if (!password.equals(repeatPassword)) {
            errors.add("passwords do not match");
        }

        if (username == null || username.isEmpty()) {
            errors.add("username is empty");
        }

        if (password.isEmpty()) {
            errors.add("password is empty");
        }
        if (errors.isEmpty()) {
            User newUser = userService.saveUser(new User(username, password));
            return "redirect:/sign-in";
        } else
            model.addAttribute("errors", errors);
        return "sign-up";
    }
}
