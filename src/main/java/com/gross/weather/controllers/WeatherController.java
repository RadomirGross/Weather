package com.gross.weather.controllers;

import com.gross.weather.exceptions.InvalidSessionTokenException;
import com.gross.weather.model.Location;
import com.gross.weather.model.Session;
import com.gross.weather.model.User;
import com.gross.weather.model.WeatherResponse;
import com.gross.weather.service.LocationService;
import com.gross.weather.service.SessionService;
import com.gross.weather.service.UserService;
import com.gross.weather.service.WeatherResponseService;
import com.gross.weather.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class WeatherController {
    private final SessionService sessionService;
    private final UserService userService;
    private final LocationService locationService;
    private final WeatherResponseService weatherResponseService;

    @Autowired
    public WeatherController(SessionService sessionService, UserService userService, LocationService locationService, WeatherResponseService weatherResponseService) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.locationService = locationService;
        this.weatherResponseService = weatherResponseService;
    }


    @GetMapping("/")
    public String index(@ModelAttribute("user") User user, Model model) {
        if (user != null) {
            List<Location> locations = locationService.findLocationsByUserId(user.getId());

            model.addAttribute("weatherResponses", weatherResponseService
                    .getWeatherResponseListFromLocations(locations));
            return "index";
        } else return "redirect:/sign-in";

    }
    }
