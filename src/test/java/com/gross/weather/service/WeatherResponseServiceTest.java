package com.gross.weather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.gross.weather.config.TestConfig;
import com.gross.weather.dto.WeatherResponseDto;
import com.gross.weather.model.Location;

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

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)

class WeatherResponseServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private Environment environment;
    private WeatherResponseService weatherResponseService;
    private MockRestServiceServer mockServer;



    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        weatherResponseService = new WeatherResponseService(restTemplate, environment);
    }

    private final List<Location> locations = List.of(
            new Location(1,"Moscow", 1, BigDecimal.valueOf(55.75), BigDecimal.valueOf(37.62)),
            new Location(2,"Barnaul", 1, BigDecimal.valueOf(53.35), BigDecimal.valueOf(83.78)),
            new Location(3,"Novosibirsk", 1, BigDecimal.valueOf(55.02), BigDecimal.valueOf(82.92)));


    @Test
    void getWeatherResponseFromLocations_parsesLargeJsonWithLocalNames_correctlyMapped() throws Exception {
        String moscowJson = Files.readString(Path.of("src/test/resources/json/weather_moscow.json"));
        String barnaulJson = Files.readString(Path.of("src/test/resources/json/weather_barnaul.json"));
        String novosibirskJson = Files.readString(Path.of("src/test/resources/json/weather_novosibirsk.json"));

        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/data/2.5/weather?lat=55.75&lon=37.62")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(moscowJson, MediaType.APPLICATION_JSON));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/data/2.5/weather?lat=53.35&lon=83.78")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(barnaulJson, MediaType.APPLICATION_JSON));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/data/2.5/weather?lat=55.02&lon=82.92")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(novosibirskJson, MediaType.APPLICATION_JSON));

       List<WeatherResponseDto> result=
               weatherResponseService.getWeatherResponseListFromLocations(locations);

        mockServer.verify();



        assertEquals(3, result.size());
        assertNotNull(result);
// Проверка первой локации (Moscow)
        WeatherResponseDto moscow = result.get(0);
        assertEquals("Moscow", moscow.getDisplayName());
        assertEquals(20.73, moscow.getFeelsLike());
        assertEquals(39,moscow.getHumidity());
        assertEquals("scattered clouds",moscow.getDescription());
        assertEquals(21.51,moscow.getTemperature());

// Проверка Barnaul
        WeatherResponseDto barnaul = result.get(1);
        assertEquals("Barnaul", barnaul.getDisplayName());
        assertEquals(22, barnaul.getFeelsLike());
        assertEquals(49,barnaul.getHumidity());
        assertEquals("few clouds",barnaul.getDescription());
        assertEquals(22.42,barnaul.getTemperature());

// Проверка Novosibirsk
        WeatherResponseDto novosibirsk = result.get(2);
        assertEquals("Novosibirsk", novosibirsk.getDisplayName());
        assertEquals(20.3, novosibirsk.getFeelsLike());
        assertEquals(56,novosibirsk.getHumidity());
        assertEquals("clear sky",novosibirsk.getDescription());
        assertEquals(20.71,novosibirsk.getTemperature());
    }

    @Test
    void getWeatherResponseFromLocation_whenLocationNotFound_returnsEmptyResult() throws Exception {
        String emptyJson = "{}";
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/data/2.5/weather?lat=0&lon=0")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(emptyJson, MediaType.APPLICATION_JSON));


        Location fakeLocation = new Location(1, "NotExistLocation", 1, BigDecimal.ZERO, BigDecimal.ZERO);
        WeatherResponseDto result = weatherResponseService.getWeatherResponseFromLocation(fakeLocation);

        mockServer.verify();

        assertNotNull(result);
        assertNotNull(result.getError());
        assertTrue(result.getError().contains("Пустой ответ от OpenWeatherMap."));
        assertEquals("NotExistLocation",result.getDisplayName());
        assertEquals(0.0,result.getFeelsLike());
        assertEquals(0,result.getHumidity());
        assertNull(result.getDescription());
        assertEquals(0.0,result.getTemperature());
    }

    @Test
    void getWeatherResponseFromLocation_whenApiKeyMissing_throwsIllegalStateException() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> weatherResponseService.getWeatherResponseFromLocation(locations.get(0)));

        assertEquals("API ключ OpenWeatherMap не найден в настройках.", exception.getMessage());
    }

    @Test
    void getWeatherResponseFromLocation_whenHttpClientError_returnsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/data/2.5/weather?lat=55.75&lon=37.62")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        WeatherResponseDto result = weatherResponseService.getWeatherResponseFromLocation(locations.get(0));

        mockServer.verify();

        assertNotNull(result);
        assertThat(result.getError()).contains("Ошибка клиента:");
    }

    @Test
    void getWeatherResponseFromLocation_whenHttpServerError_returnsErrorMessage() throws Exception {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/data/2.5/weather?lat=53.35&lon=83.78")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        WeatherResponseDto result = weatherResponseService.getWeatherResponseFromLocation(locations.get(1));

        mockServer.verify();

        assertNotNull(result);
        assertThat(result.getError()).contains("Ошибка сервера OpenWeatherMap:");
    }


    @Test
    void getWeatherResponseFromLocation_whenServerError_setsErrorMessage() {
        when(environment.getProperty("openweathermap.api.key")).thenReturn("test-key");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(containsString("/data/2.5/weather?lat=55.02&lon=82.92")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new SocketTimeoutException("Не удалось подключиться к серверу.")));

        WeatherResponseDto result = weatherResponseService.getWeatherResponseFromLocation(locations.get(2));

        assertThat(result.getError()).contains("Не удалось подключиться к серверу. Проверьте интернет-соединение.");
    }
}
