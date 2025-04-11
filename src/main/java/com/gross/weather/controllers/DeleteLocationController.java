package com.gross.weather.controllers;

import com.gross.weather.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DeleteLocationController {

    LocationService locationService;
    public DeleteLocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @PostMapping("/delete")
    public String deleteLocation(@PathVariable("locationId")  int locationId){
        locationService.delete(locationId);
        return "redirect:/";
    }
}
