package org.example.dto;

import lombok.Getter;

@Getter
public enum RegistrationError {
    LOGIN_IS_TAKEN("Данный логин уже занят"),
    INVALID_LOGIN_FORMAT("Неверный формат логина"),
    INVALID_PASSWORD_FORMAT("Неверный формат пароля");

    private final String message;
    RegistrationError(String message) {
        this.message = message;
    }
}
