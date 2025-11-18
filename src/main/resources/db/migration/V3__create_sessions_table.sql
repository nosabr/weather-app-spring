CREATE TABLE sessions (
                          ID UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          UserId BIGSERIAL NOT NULL,
                          ExpiresAt TIMESTAMP NOT NULL,
                          CONSTRAINT fk_sessions_user FOREIGN KEY (UserId) REFERENCES Users(ID) ON DELETE CASCADE
);