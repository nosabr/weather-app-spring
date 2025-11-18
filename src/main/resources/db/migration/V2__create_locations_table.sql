CREATE TABLE locations (
                           ID BIGSERIAL PRIMARY KEY,
                           Name VARCHAR(255) NOT NULL,
                           UserId BIGSERIAL NOT NULL,
                           Latitude DECIMAL(10, 8) NOT NULL,
                           Longitude DECIMAL(11, 8) NOT NULL,
                           CONSTRAINT fk_locations_user FOREIGN KEY (UserId) REFERENCES Users(ID) ON DELETE CASCADE
);