package org.example.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WeatherResponseDTO {
    private String name;           // Название города
    private String country;        // Код страны
    private BigDecimal latitude;      // Широта
    private BigDecimal longitude;     // Долгота
    private Integer temperature;   // Температура
    private Integer feelsLike;     // Ощущается как
    private Integer humidity;      // Влажность
    private String description;    // Описание погоды
    private String icon;           // Иконка погоды
}
