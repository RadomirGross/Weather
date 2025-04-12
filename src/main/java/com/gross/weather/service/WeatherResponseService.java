package com.gross.weather.service;

import com.gross.weather.model.Location;
import com.gross.weather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class WeatherResponseService {
    private final RestTemplate restTemplate;
    private final Environment environment;

@Autowired
    public WeatherResponseService(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    public WeatherResponse getWeatherResponseFromLocation(Location location) {
        String apiKey = environment.getProperty("openweathermap.api.key");
        String url=String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s",
                location.getLatitude(), location.getLongitude(), apiKey);
        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);
        if(weatherResponse!=null) {
            weatherResponse.setDisplayName(location.getName());
            weatherResponse.setLocationIdFromDB(location.getId());
        }

        return weatherResponse;
    }

    public List<WeatherResponse> getWeatherResponseListFromLocations(List<Location> locations) {
        List<WeatherResponse> weatherResponseList = new ArrayList<>();
        if (locations != null && !locations.isEmpty()) {
            for (Location location : locations) {
                weatherResponseList.add(getWeatherResponseFromLocation(location));
            }
        }
        return weatherResponseList;
    }
}
