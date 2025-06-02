package com.gross.weather.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gross.weather.model.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {


    int deleteByUserId(int userId);

    void deleteAllByExpiresAtBefore(LocalDateTime expiresAtBefore);
}
