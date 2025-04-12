package com.gross.weather.controllers;

import com.gross.weather.model.User;
import com.gross.weather.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeleteLocationController {

    LocationService locationService;
    public DeleteLocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("locationIdFromDB")  int locationId,
                                 @ModelAttribute("user") User user) {

        locationService.deleteByIdAndUserId(locationId, user.getId());
        return "redirect:/";
    }
}
