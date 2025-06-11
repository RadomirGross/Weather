package com.gross.weather.controllers;

import com.gross.weather.dto.LocationSearchResult;
import com.gross.weather.dto.SearchDto;
import com.gross.weather.service.LocationResponseService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@PropertySource("classpath:application.properties")
@Controller
public class SearchLocationController {
    private final LocationResponseService locationResponseService;

    public SearchLocationController(LocationResponseService locationResponseService) {
        this.locationResponseService = locationResponseService;
    }

    @GetMapping("/search")
    public String searchLocation(@Valid SearchDto searchDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "originPage", required = false)String originPage) {

        if (bindingResult.hasErrors() && originPage != null) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.searchDto", bindingResult);
            redirectAttributes.addFlashAttribute("searchDto", searchDto);
            return "redirect:/";
        }

        if(bindingResult.hasErrors()) {
            return "search-results";
        }
        LocationSearchResult locationSearchResult =  locationResponseService.getLocationResponseDto(searchDto.getSearch());
        model.addAttribute("locationResponsesError", locationSearchResult.getErrorMessage());
        model.addAttribute("locationResponses", locationSearchResult.getLocations());
        System.out.println(locationSearchResult.getLocations());
        model.addAttribute("search", searchDto.getSearch());
        model.addAttribute("searchDto", searchDto);
        return "search-results";


    }

}
