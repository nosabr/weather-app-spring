package org.example.service;

import org.example.dao.LocationDao;
import org.example.dao.UserDao;
import org.example.dto.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final LocationDao locationDao;
    private final UserSessionService userSessionService;
    private final UserDao userDao;

    @Autowired
    public WeatherService(RestTemplate restTemplate, LocationDao locationDao, UserSessionService userSessionService, UserDao userDao) {
        this.restTemplate = restTemplate;
        this.locationDao = locationDao;
        this.userSessionService = userSessionService;
        this.userDao = userDao;
    }

    @Value("${openweather.api.key}")
    private String API_KEY;
    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherResponseDTO getWeatherByCityName(String cityName){
        String url = String.format("%s?q=%s&appid=%s&units=metric&lang=ru",
                API_URL, cityName, API_KEY);
        try{
            @SuppressWarnings("unchecked")
            Map<String, Object> response = (Map<String, Object>) restTemplate.getForObject(url, Map.class);
            return parseWeatherResponse(response);
        }
        catch (Exception e){
            System.out.println("Error getting weather by cityName: " + cityName);
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    private WeatherResponseDTO parseWeatherResponse(Map<String, Object> response) {
        WeatherResponseDTO weatherData = new WeatherResponseDTO();

        // Название города
        weatherData.setName((String) response.get("name"));

        // Код страны
        Map<String, Object> sys = (Map<String, Object>) response.get("sys");
        weatherData.setCountry((String) sys.get("country"));

        // Координаты
        Map<String, Object> coord = (Map<String, Object>) response.get("coord");
        weatherData.setLatitude(BigDecimal.valueOf(((Number) coord.get("lat")).doubleValue()));
        weatherData.setLongitude(BigDecimal.valueOf(((Number) coord.get("lon")).doubleValue()));

        // Данные о погоде (температура, влажность и т.д.)
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        weatherData.setTemperature(((Number) main.get("temp")).intValue());
        weatherData.setFeelsLike(((Number) main.get("feels_like")).intValue());
        weatherData.setHumidity(((Number) main.get("humidity")).intValue());

        // Описание погоды и иконка
        List<Map<String, Object>> weatherList =
                (List<Map<String, Object>>) response.get("weather");

        if (weatherList != null && !weatherList.isEmpty()) {
            Map<String, Object> weather = weatherList.get(0);
            weatherData.setDescription((String) weather.get("description"));
            weatherData.setIcon((String) weather.get("icon"));
        }

        return weatherData;
    }

}
