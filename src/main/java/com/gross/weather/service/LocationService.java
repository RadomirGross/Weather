package com.gross.weather.service;

import com.gross.weather.model.Location;
import com.gross.weather.repositories.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
