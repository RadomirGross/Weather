package com.gross.weather.controllers;

import com.gross.weather.exceptions.LocationAlreadyExistsException;
import com.gross.weather.exceptions.UserNotAuthenticatedException;
import com.gross.weather.model.Location;
import com.gross.weather.model.Session;
import com.gross.weather.service.LocationService;
import com.gross.weather.service.SessionService;
import com.gross.weather.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AddLocationController {
    private final SessionService sessionService;
    private final LocationService locationService;

    public AddLocationController(SessionService sessionService, LocationService locationService) {
        this.sessionService = sessionService;
        this.locationService = locationService;
    }




    @PostMapping("/add")
    public String addLocation(@RequestParam("name") String locationName, @RequestParam("lat") BigDecimal lat,
                              @RequestParam("lon") BigDecimal lon, HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        Optional<UUID> sessionUUID = CookieUtils.extractUuidFromCookie(request, "SESSION");
        //  System.out.println("!!!!!!!!!!!!!" + lon.toString());
        if (sessionUUID.isEmpty()) {
            return "redirect:/sign-in";
        }

        try {
            Session session = sessionService.findSessionById(sessionUUID.get());
            Location location = new Location(locationName, session.getUserId(), lat, lon);
            locationService.save(location);
            return "redirect:/";
        } catch (LocationAlreadyExistsException e) {
            System.out.println("Location already exists");
            redirectAttributes.addFlashAttribute("error", "Этот город уже есть на главной странице");
            redirectAttributes.addFlashAttribute("errorLocationLat", lat.toString());
            redirectAttributes.addFlashAttribute("errorLocationLon", lon.toString());
            redirectAttributes.addFlashAttribute("errorLocationName", locationName);
            return "redirect:/search?locationName=" + URLEncoder.encode(locationName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Invalid session");
            System.out.println("qwrgwqufjskfsd");
            return "redirect:/sign-in";
        }
    }


}

