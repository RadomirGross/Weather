package com.gross.weather.service;

import com.gross.weather.config.TestConfig;
import com.gross.weather.model.Session;
import com.gross.weather.model.User;
import com.gross.weather.repositories.SessionRepository;
import com.gross.weather.testcontainer.PostgresContainerHolder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Rollback
class SessionServiceTest {

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",
                PostgresContainerHolder.POSTGRES_CONTAINER::getJdbcUrl);
        r.add("spring.datasource.username",
                PostgresContainerHolder.POSTGRES_CONTAINER::getUsername);
        r.add("spring.datasource.password",
                PostgresContainerHolder.POSTGRES_CONTAINER::getPassword);
    }

    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void saveSession_persistsAndReturnsUuid() {


        Session saved = sessionService.saveSession(new Session(1));
        em.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getExpiresAt()).isAfter(LocalDateTime.now());
        assertThat(sessionRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void expiredSession_isRemoved() {

        Session expired = new Session(2);
        expired.setExpiresAt(LocalDateTime.now().minusHours(1));
        sessionRepository.save(expired);
        System.out.println("Session ===:"+expired);
        assertThat(sessionRepository.findById(expired.getId())).isPresent();

        sessionService.deleteSession(expired);

        assertThat(sessionRepository.findById(expired.getId())).isEmpty();
    }

    @Test
    void deleteExpiredSessions_removesOnlyExpiredSessions() {
        Session expired = new Session(3);
        expired.setExpiresAt(LocalDateTime.now().minusHours(1));
        sessionRepository.save(expired);

        Session valid = new Session(4);
        valid.setExpiresAt(LocalDateTime.now().plusHours(1));
        sessionRepository.save(valid);
        sessionRepository.flush();

        assertThat(sessionRepository.findById(valid.getId())).isPresent();
        System.out.println("Valid expiresAt: " + valid.getExpiresAt());
        System.out.println("Now: " + LocalDateTime.now());
        sessionService.deleteExpiredSessions();
        sessionRepository.flush();

        assertThat(sessionRepository.findById(expired.getId())).isEmpty();
        assertThat(sessionRepository.findById(valid.getId())).isPresent();

    }

}
