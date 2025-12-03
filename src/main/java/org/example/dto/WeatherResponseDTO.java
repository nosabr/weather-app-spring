package org.example.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.weather.Coord;
import org.example.dto.weather.Main;
import org.example.dto.weather.Weather;
import org.example.dto.weather.Wind;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WeatherResponseDTO {
    private Coord coord;
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private String name;
}
