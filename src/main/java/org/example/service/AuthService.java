package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.UserSessionDao;
import org.example.model.User;
import org.example.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserSessionDao userSessionDao;
    private final UserDao userDao;

    @Autowired
    public AuthService(UserSessionDao userSessionDao, UserDao userDao) {
        this.userSessionDao = userSessionDao;
        this.userDao = userDao;
    }

    public User getUserBySessionId(UUID sessionId) {
        Optional<UserSession> session = userSessionDao.findById(sessionId);
        return session.map(UserSession::getUser).orElse(null);
    }
}
