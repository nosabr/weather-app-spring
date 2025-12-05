package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.Location;


@Getter
@AllArgsConstructor
public class LocationAddResultDTO {
    private final boolean isSuccess;
    private final Location location;
    private final LocationError error;

    public static LocationAddResultDTO success(Location location) {
        return new LocationAddResultDTO(true, location, null);
    }

    public static LocationAddResultDTO failure(LocationError error) {
        return new LocationAddResultDTO(false, null, error);
    }
}

