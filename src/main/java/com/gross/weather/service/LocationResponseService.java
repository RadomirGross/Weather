package com.gross.weather.service;

import com.gross.weather.model.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class LocationResponseService {

    private final RestTemplate restTemplate;
    private final Environment environment;

@Autowired
    public LocationResponseService(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment=environment;
    }

    public List<LocationResponse> getLocationResponse(String locationName) {
        String apiKey=environment.getProperty("openweathermap.api.key");
        String url =String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s",
                locationName, apiKey);
        LocationResponse[] locationResponse = restTemplate.getForObject(url, LocationResponse[].class);
        return locationResponse!=null?Arrays.stream(locationResponse).toList(): Collections.emptyList();
    }
}
