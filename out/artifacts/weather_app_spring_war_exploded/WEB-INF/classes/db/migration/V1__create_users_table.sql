-- Создание таблицы пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       login VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- Индекс для быстрого поиска по логину
CREATE INDEX idx_users_login ON users(login);