package com.gross.weather.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.*;

import com.gross.weather.config.TestConfig;
import com.gross.weather.dto.LocationResponseDto;
import com.gross.weather.dto.LocationSearchResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)

public class LocationResponseServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private Environment environment;
    private LocationResponseService service;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        service = new LocationResponseService(restTemplate, environment);
    }


    @Test
    void getLocationResponseDto_parsesLargeJsonWithLocalNames_correctlyMapped() throws Exception {
        String json = Files.readString(Path.of("src/test/resources/json/location_moscow.json"));
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/geo/1.0/direct?q=Moscow")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        LocationSearchResult result = service.getLocationResponseDto("Moscow");

        mockServer.verify();


        List<LocationResponseDto> locations = result.getLocations();
        assertEquals(5, locations.size());
        assertNotNull(result);
        assertNull(result.getErrorMessage());
        assertThat(locations.get(0).getName()).isEqualTo("Moscow");
        assertThat(locations.get(0).getCountry()).isEqualTo("RU");
        assertThat(locations.get(0).getState()).isEqualTo("Moscow");
        assertThat(locations.get(1).getName()).isEqualTo("Moscow");
        assertThat(locations.get(1).getCountry()).isEqualTo("US");
        assertThat(locations.get(1).getState()).isEqualTo("Idaho");
        assertThat(locations.get(2).getName()).isEqualTo("Moscow");
        assertThat(locations.get(2).getCountry()).isEqualTo("US");
        assertThat(locations.get(2).getState()).isEqualTo("Maine");
        assertThat(locations.get(3).getName()).isEqualTo("Moscow");
        assertThat(locations.get(3).getCountry()).isEqualTo("US");
        assertThat(locations.get(3).getState()).isEqualTo("Tennessee");
        assertThat(locations.get(4).getName()).isEqualTo("Moscow");
        assertThat(locations.get(4).getCountry()).isEqualTo("US");
        assertThat(locations.get(4).getState()).isEqualTo("Maryland");
    }

    @Test
    void getLocationResponseDto_whenLocationNotFound_returnsEmptyResult() throws Exception {
        String emptyJson = "[]";
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/geo/1.0/direct?q=cityIsNotExist")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(emptyJson, MediaType.APPLICATION_JSON));

        LocationSearchResult result = service.getLocationResponseDto("cityIsNotExist");

        mockServer.verify();

        assertNotNull(result);
        assertTrue(result.getErrorMessage().contains("Для этого запроса не найдено локаций."));
        assertThat(result.getLocations()).isEmpty();
    }

    @Test
    void getLocationResponseDto_whenApiKeyMissing_throwsIllegalStateException() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.getLocationResponseDto("Moscow"));

        assertEquals("API ключ OpenWeatherMap не найден в настройках.", exception.getMessage());
    }

    @Test
    void getLocationResponseDto_whenHttpClientError_returnsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/geo/1.0/direct?q=BadRequest")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        LocationSearchResult result = service.getLocationResponseDto("BadRequest");

        mockServer.verify();

        assertNotNull(result);
        assertThat(result.getErrorMessage()).contains("Ошибка клиента при запросе локаций:");
        assertThat(result.getLocations()).isNullOrEmpty();
    }

    @Test
    void getLocationResponseDto_whenHttpServerError_returnsErrorMessage() throws Exception {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/geo/1.0/direct?q=ServerError")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        LocationSearchResult result = service.getLocationResponseDto("ServerError");

        mockServer.verify();

        assertNotNull(result);
        assertThat(result.getErrorMessage()).contains("Ошибка сервера OpenWeatherMap:");
        assertThat(result.getLocations()).isNullOrEmpty();
    }


    @Test
    void getLocationResponseDto_whenServerError_setsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/geo/1.0/direct?q=ServerError")))
                 .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new SocketTimeoutException("Не удалось подключиться к серверу.")));

        LocationSearchResult result = service.getLocationResponseDto("ServerError");

        assertTrue(result.getErrorMessage().contains("Не удалось подключиться к серверу. Проверьте интернет-соединение:"));
    }
}
