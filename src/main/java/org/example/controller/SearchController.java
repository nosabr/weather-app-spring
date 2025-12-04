package org.example.controller;

import org.example.dto.WeatherResponseDTO;
import org.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    WeatherService weatherService;

    @Autowired
    public SearchController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "cityName", required = false) String cityName, Model model) {
        if(cityName == null || cityName.isEmpty()){
            model.addAttribute("error","Пустой запрос");
            return "search-results";
        }
        model.addAttribute("cityName", cityName);
        WeatherResponseDTO weatherResponseDTO = weatherService.getWeatherByCityName(cityName);
        if(weatherResponseDTO == null){
            model.addAttribute("error", "Город не найден");
            return "search-results";
        }
        model.addAttribute("weatherResponseDTO", weatherResponseDTO);
        return "search-results";
    }
}
