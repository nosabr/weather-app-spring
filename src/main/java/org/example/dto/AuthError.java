package org.example.dto;

import lombok.Getter;

@Getter
public enum AuthError {
    USER_NOT_FOUND("Пользователь с таким логином не найден"),
    WRONG_PASSWORD("Неверный пароль");

    private final String message;

    AuthError(String message) {
        this.message = message;
    }
}
