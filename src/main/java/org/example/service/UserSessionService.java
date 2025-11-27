package org.example.service;

import org.example.dao.UserSessionDao;
import org.example.model.User;
import org.example.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserSessionService {
    private final UserSessionDao userSessionDao;
    @Autowired
    public UserSessionService(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    public User getUserBySessionId(UUID sessionId) {
        Optional<UserSession> session = userSessionDao.findById(sessionId);
        return session.map(UserSession::getUser).orElse(null);
    }
}
