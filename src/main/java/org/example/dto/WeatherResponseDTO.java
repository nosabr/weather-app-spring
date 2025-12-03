package org.example.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WeatherResponseDTO {
    private String name;           // Название города
    private String country;        // Код страны
    private Integer temperature;   // Температура
    private Integer feelsLike;     // Ощущается как
    private Integer humidity;      // Влажность
    private String description;    // Описание погоды
    private String icon;           // Иконка погоды
}
