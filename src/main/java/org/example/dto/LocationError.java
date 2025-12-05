package org.example.dto;

import lombok.Getter;

@Getter
public enum LocationError {
    USER_NOT_FOUND("Пользователь не найден"),
    CITY_NOT_FOUND("Город не найден"),
    ALREADY_ADDED("Этот город уже добавлен в список"),
    INVALID_DATA("Неверные данные");

    private final String message;

    LocationError(String message) {
        this.message = message;
    }
}