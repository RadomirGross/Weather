package com.gross.weather.controllers;

import com.gross.weather.model.LocationResponse;
import com.gross.weather.service.LocationResponseService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@PropertySource("classpath:application.properties")
@Controller
public class SearchLocationController {
    private final LocationResponseService locationResponseService;

    public SearchLocationController(LocationResponseService locationResponseService) {
        this.locationResponseService = locationResponseService;
    }

    @GetMapping("/search")
    public String searchLocation(@RequestParam(name = "locationName") String locationName, Model model) {
        List<LocationResponse> locationResponses = locationResponseService.getLocationResponse(locationName);
        model.addAttribute("locationResponses", locationResponses);
        model.addAttribute("locationName", locationName);
        return "search-results";
    }

}
