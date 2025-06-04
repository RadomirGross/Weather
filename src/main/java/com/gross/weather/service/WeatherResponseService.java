package com.gross.weather.service;

import com.gross.weather.dto.WeatherResponseDto;
import com.gross.weather.mapper.WeatherResponseMapper;
import com.gross.weather.model.Location;
import com.gross.weather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.ArrayList;
import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class WeatherResponseService {
    private final RestTemplate restTemplate;
    private final Environment environment;
    private final WeatherResponseMapper mapper;

    @Autowired
    public WeatherResponseService(RestTemplate restTemplate, Environment environment, WeatherResponseMapper mapper) {
        this.restTemplate = restTemplate;
        this.environment = environment;
        this.mapper = WeatherResponseMapper.INSTANCE;
    }

    public WeatherResponseDto getWeatherResponseFromLocation(Location location) {
        String apiKey = environment.getProperty("openweathermap.api.key");

        if (apiKey == null) {
            throw new IllegalStateException("API ключ OpenWeatherMap не найден в настройках.");
        }

        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s", location.getLatitude(), location.getLongitude(), apiKey);

        try {
            WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

            if (weatherResponse != null) {
                weatherResponse.setDisplayName(location.getName());
                weatherResponse.setLocationIdFromDB(location.getId());
                return mapper.toWeatherResponseDto(weatherResponse);

            } else {
                return buildErrorWeatherDto(location, "Пустой ответ от OpenWeatherMap.");
            }
        } catch (HttpClientErrorException e) {
            return buildErrorWeatherDto(location, "Ошибка клиента: " + e.getStatusCode());
        } catch (HttpServerErrorException e) {
            return buildErrorWeatherDto(location, "Ошибка сервера OpenWeatherMap: " + e.getStatusCode() + ". Попробуйте позже.");
        } catch (ResourceAccessException e) {
            return buildErrorWeatherDto(location, "Не удалось подключиться к серверу. Проверьте интернет-соединение.");
        } catch (Exception e) {
            return buildErrorWeatherDto(location, "Неизвестная ошибка при получении погоды.");
        }
    }

    public List<WeatherResponseDto> getWeatherResponseListFromLocations(List<Location> locations) {
        List<WeatherResponseDto> weatherResponseDtoList = new ArrayList<>();
        if (locations != null && !locations.isEmpty()) {
            for (Location location : locations) {
                weatherResponseDtoList.add(getWeatherResponseFromLocation(location));
            }
        }
        return weatherResponseDtoList;
    }

    public WeatherResponseDto buildErrorWeatherDto(Location location, String error) {
        WeatherResponseDto dto = new WeatherResponseDto();
        dto.setDisplayName(location.getName());
        dto.setError(error);
        return dto;
    }
}
