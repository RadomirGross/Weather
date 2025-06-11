package com.gross.weather.service;

import com.gross.weather.exceptions.LocationAlreadyExistsException;
import com.gross.weather.model.Location;
import com.gross.weather.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    public List<Location> findLocationsByUserId(int userId) {
        return locationRepository.findByUserId(userId);
    }

    public long deleteByIdAndUserId(int id, int userId) {
        return locationRepository.deleteByIdAndUserId(id, userId);
    }


    public Location save(Location location) {
        Optional<Location> existing = locationRepository
                .findLocationByUserIdAndLatitudeAndLongitude(location.getUserId(), location.getLatitude(), location.getLongitude());

        if (existing.isPresent()) {
            throw new LocationAlreadyExistsException("Такая локация уже существует у этого пользователя.");
        }

        return locationRepository.save(location);
    }
}
