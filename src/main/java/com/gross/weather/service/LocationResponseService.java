package com.gross.weather.service;

import com.gross.weather.dto.LocationSearchResult;
import com.gross.weather.mapper.LocationResponseMapper;
import com.gross.weather.model.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class LocationResponseService {

    private final RestTemplate restTemplate;
    private final Environment environment;
    private final Logger logger;
    private final LocationResponseMapper mapper;

    @Autowired
    public LocationResponseService(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
        this.logger = LoggerFactory.getLogger(LocationResponseService.class);
        this.mapper = LocationResponseMapper.INSTANCE;
    }

    public LocationSearchResult getLocationResponseDto(String locationName) {
        String apiKey = environment.getProperty("openweathermap.api.key");
        LocationSearchResult locationSearchResult = new LocationSearchResult();

        if (apiKey == null) {
            throw new IllegalStateException("API ключ OpenWeatherMap не найден в настройках.");
        }

        String url = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s",
                locationName, apiKey);
        try {
            LocationResponse[] locationResponse = restTemplate.getForObject(url, LocationResponse[].class);

            if (locationResponse == null || locationResponse.length == 0) {
                locationSearchResult.setLocations(Collections.emptyList());
                locationSearchResult.setErrorMessage("Для этого запроса не найдено локаций.");
                return locationSearchResult;
            } else locationSearchResult.setLocations(mapper.toLocationResponseDtoList(
                    Arrays.stream(locationResponse).toList()));
        } catch (HttpClientErrorException e) {
            locationSearchResult.setErrorMessage("Ошибка клиента при запросе локаций:" + e.getStatusCode() +
                    " " + e.getResponseBodyAsString());
            logger.warn("Ошибка клиента при запросе локаций: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return locationSearchResult;
        } catch (HttpServerErrorException e) {
            locationSearchResult.setErrorMessage("Ошибка сервера OpenWeatherMap: " + e.getStatusCode()
                    + " " + e.getResponseBodyAsString());
            logger.warn("Ошибка сервера OpenWeatherMap: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return locationSearchResult;
        } catch (ResourceAccessException e) {
            locationSearchResult.setErrorMessage("Не удалось подключиться к серверу. Проверьте интернет-соединение: " + e.getMessage());
            logger.warn("Не удалось подключиться к серверу. Проверьте интернет-соединение: {}", e.getMessage());
            return locationSearchResult;
        } catch (Exception e) {
            e.printStackTrace();
            locationSearchResult.setErrorMessage("Неизвестная ошибка при запросе локаций: " + e.getMessage());
            logger.error("Неизвестная ошибка при запросе локаций: ", e);
            return locationSearchResult;
        }
        return locationSearchResult;
    }
}
