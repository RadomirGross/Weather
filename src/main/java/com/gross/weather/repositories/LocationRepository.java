package com.gross.weather.repositories;

import com.gross.weather.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByUserId(int userId);

    Optional<Location> findLocationByUserIdAndLatitudeAndLongitude(int userId, BigDecimal latitude, BigDecimal longitude);

    long deleteByIdAndUserId(Integer id, int userId);

}
