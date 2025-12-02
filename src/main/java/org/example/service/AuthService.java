package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.UserSessionDao;
import org.example.dto.AuthError;
import org.example.dto.AuthResultDTO;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.util.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResultDTO authenticate(String login, String password) {
        Optional<User> optUser = userDao.findByLogin(login);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (passwordEncoder.check(password, user.getPassword())) {
                System.out.println("[AuthService] User logged successfully");
                return AuthResultDTO.success(user);
            }
            System.out.println("[AuthService] Wrong password");
            return AuthResultDTO.failure(AuthError.WRONG_PASSWORD);
        }
        System.out.println("[AuthService] User doesn't exist");
        return AuthResultDTO.failure(AuthError.USER_NOT_FOUND);
    }
}
