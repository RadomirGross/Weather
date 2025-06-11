package com.gross.weather.service;

import com.gross.weather.config.TestConfig;
import com.gross.weather.exceptions.LocationAlreadyExistsException;
import com.gross.weather.model.Location;
import com.gross.weather.repositories.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
@Rollback
@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void findLocationsByUserId_returnsLocations() {
        int userId = 1;
        List<Location> locations = List.of(new Location("Moscow", 1,BigDecimal.valueOf(1),BigDecimal.valueOf(1)),
                                           new Location("London", 1,BigDecimal.valueOf(2),BigDecimal.valueOf(3)));
        when(locationRepository.findByUserId(userId)).thenReturn(locations);

        List<Location> result = locationService.findLocationsByUserId(userId);

        assertEquals(locations, result);
    }

    @Test
    void deleteByIdAndUserId_returnsDeletedCount() {
        int id = 10;
        int userId = 1;
        when(locationRepository.deleteByIdAndUserId(id, userId)).thenReturn(1L);

        long deletedCount = locationService.deleteByIdAndUserId(id, userId);

        assertEquals(1L, deletedCount);
    }

    @Test
    void save_newLocation_savesSuccessfully() {
        Location location = new Location("Moscow", 1,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        when(locationRepository.findLocationByUserIdAndLatitudeAndLongitude(
                location.getUserId(), location.getLatitude(), location.getLongitude()))
                .thenReturn(Optional.empty());
        when(locationRepository.save(location)).thenReturn(location);

        Location saved = locationService.save(location);

        assertEquals(location, saved);
    }

    @Test
    void save_existingLocation_throwsException() {
        Location location = new Location("Moscow", 1,BigDecimal.valueOf(1),BigDecimal.valueOf(1));
        when(locationRepository.findLocationByUserIdAndLatitudeAndLongitude(
                location.getUserId(), location.getLatitude(), location.getLongitude()))
                .thenReturn(Optional.of(location));

        assertThrows(LocationAlreadyExistsException.class, () -> locationService.save(location));
    }
}
