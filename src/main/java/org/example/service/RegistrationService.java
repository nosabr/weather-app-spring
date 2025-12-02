package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.UserSessionDao;
import org.example.dto.RegistrationError;
import org.example.dto.RegistrationResultDTO;
import org.example.model.User;
import org.example.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private static final String LOGIN_FORMAT = "^[A-Za-z0-9]{6,}$";
    private static final String PASSWORD_FORMAT = "^.{6,}$";

    private final UserSessionDao userSessionDao;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserSessionDao userSessionDao, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userSessionDao = userSessionDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public RegistrationResultDTO register(String login, String password) {
        if(!isLoginValid(login)) {
            return RegistrationResultDTO.failure(RegistrationError.INVALID_LOGIN_FORMAT);
        }
        if(!isPasswordValid(password)) {
            return  RegistrationResultDTO.failure(RegistrationError.INVALID_PASSWORD_FORMAT);
        }
        if(isLoginTaken(login)) {
            return  RegistrationResultDTO.failure(RegistrationError.LOGIN_IS_TAKEN);
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = userDao.save(new User(login,hashedPassword));
        return   RegistrationResultDTO.success(user);
    }

    private boolean isLoginTaken(String login) {
        return userDao.isUserExist(login);
    }

    private boolean isLoginValid(String login){
        return login.matches(LOGIN_FORMAT);
    }

    private boolean isPasswordValid(String password){
        return password.matches(PASSWORD_FORMAT);
    }
}
