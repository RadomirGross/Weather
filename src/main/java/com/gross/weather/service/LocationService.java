package com.gross.weather.service;

import com.gross.weather.exceptions.LocationAlreadyExistsException;
import com.gross.weather.model.Location;
import com.gross.weather.repositories.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(int id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Локация с id " + id + " не найдена"));
    }

    public List<Location> findLocationsByUserId(int userId) {
        return locationRepository.findByUserId(userId);
    }


    public void delete(int locationId) {
        locationRepository.deleteById(locationId);
    }

    public long deleteByIdAndUserId(int id, int userId) {
        return locationRepository.deleteByIdAndUserId(id, userId);
    }


    public Location save(Location location) {
        Optional<Location> existing = locationRepository
                .findLocationByUserIdAndLatitudeAndLongitude(location.getUserId(), location.getLatitude(), location.getLongitude());
        System.out.println("location id " + "------------------------------------------------");
        System.out.println("Latitude"+location.getLatitude());
        System.out.println("Longitude"+location.getLongitude());
        System.out.println("Latitude"+location.getLatitude().toString());
        System.out.println("Longitude"+location.getLongitude().toString());

        if (existing.isPresent()) {
            System.out.println("location id " + "----=======================-------------------------");
            throw new LocationAlreadyExistsException("Такая локация уже существует у этого пользователя.");
        }

        return locationRepository.save(location);
    }
}
