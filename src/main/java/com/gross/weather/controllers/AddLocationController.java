package com.gross.weather.controllers;

import com.gross.weather.exceptions.LocationAlreadyExistsException;
import com.gross.weather.model.Location;
import com.gross.weather.model.User;
import com.gross.weather.service.LocationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class AddLocationController {
    private final LocationService locationService;

    public AddLocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/add")
    public String addLocation(@ModelAttribute("user") User user, @RequestParam("name") String locationName, @RequestParam("lat") BigDecimal lat,
                              @RequestParam("lon") BigDecimal lon,@RequestParam("search")String search,
                              RedirectAttributes redirectAttributes) {

        try {
            Location location = new Location(locationName, user.getId(), lat, lon);
            locationService.save(location);
            return "redirect:/";
        } catch (LocationAlreadyExistsException e) {
            System.out.println("Location already exists");
            redirectAttributes.addFlashAttribute("error", "Этот город уже есть на главной странице");
            redirectAttributes.addFlashAttribute("errorLocationLat", lat.toString());
            redirectAttributes.addFlashAttribute("errorLocationLon", lon.toString());

            return "redirect:/search?search=" + URLEncoder.encode(search, StandardCharsets.UTF_8);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Invalid session");
            return "redirect:/sign-in";
        }
    }


}

