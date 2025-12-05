package org.example.service;

import org.example.dao.UserSessionDao;
import org.example.model.User;
import org.example.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSessionService {
    private final UserSessionDao userSessionDao;

    @Value("${session.duration.seconds:3600}") // 1 HOUR
    private long SESSION_DURATION_SECONDS;

    @Autowired
    public UserSessionService(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    public User getUserBySessionId(UUID sessionId) {
        try{
            Optional<UserSession> session = userSessionDao.findById(sessionId);
            return session.map(UserSession::getUser).orElse(null);
        }
        catch (IllegalArgumentException | NullPointerException e){
            return null;
        }
    }
    @Transactional
    public UserSession createUserSession(User user) {
        Instant expiresAt = Instant.now().plusSeconds(SESSION_DURATION_SECONDS);
        UserSession userSession = userSessionDao.save(new UserSession(user, expiresAt));
        System.out.println("[UserSessionService] UserSession created]");
        return userSession;
    }

    public void deleteUserSession(String sessionId) {
        try {
            UUID uuid = UUID.fromString(sessionId);
            userSessionDao.findById(uuid).ifPresent(userSessionDao::delete);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid session ID: " + sessionId);
        }
    }
}
