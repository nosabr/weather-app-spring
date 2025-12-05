package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("coord")
    private Coordinates coord;

    @JsonProperty("weather")
    private List<Weather> weather;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("sys")
    private Sys sys;

    // Вложенные классы
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coordinates {
        @JsonProperty("lat")
        private BigDecimal lat;

        @JsonProperty("lon")
        private BigDecimal lon;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        @JsonProperty("description")
        private String description;

        @JsonProperty("icon")
        private String icon;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private Integer temp;

        @JsonProperty("feels_like")
        private Integer feelsLike;

        @JsonProperty("humidity")
        private Integer humidity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        @JsonProperty("country")
        private String country;
    }

    // Удобные методы для быстрого доступа (опционально)
    public BigDecimal getLatitude() {
        return coord != null ? coord.getLat() : null;
    }

    public BigDecimal getLongitude() {
        return coord != null ? coord.getLon() : null;
    }

    public String getDescription() {
        return weather != null && !weather.isEmpty() ? weather.get(0).getDescription() : null;
    }

    public String getIcon() {
        return weather != null && !weather.isEmpty() ? weather.get(0).getIcon() : null;
    }

    public Integer getTemperature() {
        return main != null ? main.getTemp() : null;
    }

    public Integer getFeelsLike() {
        return main != null ? main.getFeelsLike() : null;
    }

    public Integer getHumidity() {
        return main != null ? main.getHumidity() : null;
    }

    public String getCountry() {
        return sys != null ? sys.getCountry() : null;
    }
}