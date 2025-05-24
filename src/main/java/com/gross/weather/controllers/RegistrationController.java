package com.gross.weather.controllers;

import com.gross.weather.dto.UserDto;
import com.gross.weather.model.User;
import com.gross.weather.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {

        model.addAttribute("userDto", new UserDto());
        System.out.println("Sign up!!!!!!!!!!!!!!!!");
        return "sign-up";
    }

    @PostMapping("/sign-up3")
    public String signUp3(@RequestParam("username") String username, @RequestParam("password") String password,
                          @RequestParam("repeat-password") String repeatPassword, Model model) {

        List<String> errors = new ArrayList<>();
        if (userService.findUserByLogin(username) != null) {
            errors.add("login already exists");
            model.addAttribute("errors", errors);
            return "sign-up3";
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
            userService.saveUser(new User(username, password));
            return "redirect:/sign-in";
        } else
            model.addAttribute("errors", errors);
        return "sign-up3";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("userDto") @Valid UserDto userDto
            , BindingResult bindingResult) {
        // Проверка совпадения паролей

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty())
            if (!userDto.getPassword().equals(userDto.getRepeatPassword())) {
                bindingResult.rejectValue("repeatPassword", "", "Пароли не совпадают");
            }

        // Проверка на существующий логин (пример)
        if (userService.findUserByLogin(userDto.getLogin()) != null) {
            bindingResult.rejectValue("login", "", "Такой логин уже существует");
        }

        if (bindingResult.hasErrors()) {
            return "sign-up"; // страница формы
        }

        // Преобразовать DTO в User
        userService.register(userDto);

        return "redirect:/sign-in";

    }
}
