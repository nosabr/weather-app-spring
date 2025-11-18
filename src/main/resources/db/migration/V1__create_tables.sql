-- Создание таблицы пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       login VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- Индекс для быстрого поиска по логину
CREATE INDEX idx_users_login ON users(login);

-- Создание таблицы сессий
CREATE TABLE sessions (
                          id UUID PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          expires_at TIMESTAMP NOT NULL,
                          CONSTRAINT fk_sessions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Индекс для быстрого поиска сессий по user_id
CREATE INDEX idx_sessions_user_id ON sessions(user_id);

-- Индекс для быстрого удаления истекших сессий
CREATE INDEX idx_sessions_expires_at ON sessions(expires_at);

-- Создание таблицы локаций
CREATE TABLE locations (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           user_id BIGINT NOT NULL,
                           latitude DECIMAL(10, 8) NOT NULL,
                           longitude DECIMAL(11, 8) NOT NULL,
                           CONSTRAINT fk_locations_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Уникальный индекс: один пользователь не может добавить одну локацию дважды
CREATE UNIQUE INDEX idx_locations_user_coordinates ON locations(user_id, latitude, longitude);

-- Индекс для быстрого поиска локаций пользователя
CREATE INDEX idx_locations_user_id ON locations(user_id);