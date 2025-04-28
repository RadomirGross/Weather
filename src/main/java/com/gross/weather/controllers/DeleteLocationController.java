package com.gross.weather.controllers;

import com.gross.weather.model.User;
import com.gross.weather.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteLocationController {

    LocationService locationService;
    public DeleteLocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("locationIdFromDB") int locationId,
                                 @ModelAttribute("user") User user,
                                 RedirectAttributes redirectAttributes) {

        long deleted = locationService.deleteByIdAndUserId(locationId, user.getId());
        if (locationId == deleted) {
            redirectAttributes.addFlashAttribute("badLocationId", locationId);
            redirectAttributes.addFlashAttribute("error", "Не удалось удалить локацию.");
        }

        return "redirect:/";
    }
}
