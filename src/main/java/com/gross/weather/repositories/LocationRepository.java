package com.gross.weather.repositories;

import com.gross.weather.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByUserId(int userId);

    Optional<Location> findLocationByUserIdAndLatitudeAndLongitude(int userId, BigDecimal latitude, BigDecimal longitude);

    long deleteByIdAndUserId(Integer id, int userId);

    long deleteByUserIdAndLatitude(int userId, BigDecimal latitude);

    @Transactional
    @Modifying
    @Query("delete from Location l where l.id = ?1 and l.userId = ?2 and l.latitude = ?3 and l.longitude = ?4")
    int deleteByIdAndUserIdAndLatitudeAndLongitude(Integer id, int userId, BigDecimal latitude, BigDecimal longitude);
}
