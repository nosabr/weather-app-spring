package org.example.service;

import org.example.dao.UserSessionDao;
import org.example.model.User;
import org.example.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSessionService {
    private final UserSessionDao userSessionDao;
    private static final long SESSION_DURATION_HOURS = 1;

    @Autowired
    public UserSessionService(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    public User getUserBySessionId(UUID sessionId) {
        Optional<UserSession> session = userSessionDao.findById(sessionId);
        return session.map(UserSession::getUser).orElse(null);
    }

    public UserSession createUserSession(User user) {
        UUID uuid = UUID.randomUUID();
        Instant expiresAt = Instant.now().plusSeconds(SESSION_DURATION_HOURS * 3600);
        UserSession userSession = new UserSession(user, expiresAt);
        return userSessionDao.save(userSession);
    }

}
