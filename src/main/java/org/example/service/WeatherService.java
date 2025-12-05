package org.example.service;

import org.example.dto.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${openweather.api.key}")
    private String API_KEY;

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponseDTO getWeatherByCityName(String cityName) {
        String url = String.format("%s?q=%s&appid=%s&units=metric&lang=ru",
                API_URL, cityName, API_KEY);
        try {
            return restTemplate.getForObject(url, WeatherResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("City Not Found");
            return null;
        } catch (Exception e) {
            System.out.println("Error getting weather for city: " + cityName);
            return null;
        }
    }
}