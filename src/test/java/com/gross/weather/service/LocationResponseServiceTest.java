package com.gross.weather.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.gross.weather.config.TestConfig;
import com.gross.weather.dto.LocationSearchResult;
import com.gross.weather.model.LocationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Rollback
@ExtendWith(MockitoExtension.class)
public class LocationResponseServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Environment environment;

    private LocationResponseService service;

    @BeforeEach
    void setUp() {
        service = new LocationResponseService(restTemplate, environment);
    }

    @Test
    void getLocationResponseDto_successfulResponse_returnsLocationList() {
        // Given
        String locationName = "Moscow";
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-api-key");

        LocationResponse[] fakeResponse = new LocationResponse[] {
                new LocationResponse("Moscow", "RU", "55.75", "37.62")
        };

        when(restTemplate.getForObject(anyString(), eq(LocationResponse[].class)))
                .thenReturn(fakeResponse);

        // When
        LocationSearchResult result = service.getLocationResponseDto(locationName);

        // Then
        assertNotNull(result);
        assertNull(result.getErrorMessage());
        assertEquals(1, result.getLocations().size());
        assertEquals("Moscow", result.getLocations().get(0).getName());
    }

    @Test
    void getLocationResponseDto_whenNoLocationsFound_setsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-api-key");
        when(restTemplate.getForObject(anyString(), eq(LocationResponse[].class)))
                .thenReturn(new LocationResponse[0]);

        LocationSearchResult result = service.getLocationResponseDto("UnknownCity");

        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Для этого запроса не найдено локаций."));
    }

    @Test
    void getLocationResponseDto_whenClientError_setsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-api-key");

        when(restTemplate.getForObject(anyString(), eq(LocationResponse[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request"));

        LocationSearchResult result = service.getLocationResponseDto("badCity");

        assertTrue(result.getErrorMessage().contains("Ошибка клиента при запросе локаций:"));
    }

    @Test
    void getLocationResponseDto_whenServerError_setsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-api-key");

        when(restTemplate.getForObject(anyString(), eq(LocationResponse[].class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"));

        LocationSearchResult result = service.getLocationResponseDto("any");

        assertTrue(result.getErrorMessage().contains("Ошибка сервера OpenWeatherMap:"));
    }

    @Test
    void getLocationResponseDto_whenNetworkError_setsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-api-key");

        when(restTemplate.getForObject(anyString(), eq(LocationResponse[].class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        LocationSearchResult result = service.getLocationResponseDto("any");

        assertTrue(result.getErrorMessage().contains("Не удалось подключиться к серверу OpenWeatherMap:"));
    }

    @Test
    void getLocationResponseDto_whenApiKeyMissing_throwsException() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn(null);

        assertThrows(IllegalStateException.class,
                () -> service.getLocationResponseDto("city"));

    }


}
