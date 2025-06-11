package com.gross.weather.service;

import com.gross.weather.model.Session;
import com.gross.weather.repositories.SessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SessionService {
    private  final int SESSION_DURATION_HOURS = 1;
    private  final int SESSION_DURATION_MINUTES = 10;
    private static final long ONE_HOUR_IN_MILLISECONDS = 60 * 60 * 1000;
    private final SessionRepository sessionRepository;


    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    public Session findSessionById(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElse(null);
    }


    @Transactional
    public Session saveSession(Session session) {
        session.setExpiresAt(LocalDateTime.now().plusHours(SESSION_DURATION_HOURS));
        return sessionRepository.save(session);
    }

    @Transactional
    public void deleteSession(Session session) {
        sessionRepository.delete(session);
    }

    @Transactional
    public int deleteByUserId(int userId) {
        return sessionRepository.deleteByUserId(userId);
    }

    @Scheduled(fixedRate = ONE_HOUR_IN_MILLISECONDS)
    public void deleteExpiredSessions() {
        sessionRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}
