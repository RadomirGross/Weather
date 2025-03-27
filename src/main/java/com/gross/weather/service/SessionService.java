package com.gross.weather.service;

import com.gross.weather.model.Session;
import com.gross.weather.repositories.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;


    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Session findSessionBiId(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElse(null);
    }


    @Transactional
    public Session saveSession(Session session) {
        session.setExpiresAt(LocalDateTime.now().plusHours(1));
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

}
