package com.gross.weather.advice;

import com.gross.weather.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute("user")
    public User addUserToModel(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }
}