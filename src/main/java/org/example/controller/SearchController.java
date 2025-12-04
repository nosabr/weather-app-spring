package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.WeatherResponseDTO;
import org.example.service.WeatherService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    WeatherService weatherService;

    @Autowired
    public SearchController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/search")
    public String search(HttpServletRequest request,
                         @RequestParam(value = "cityName", required = false) String cityName,
                         Model model) {

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
        if(isCityAlreadyAdded(weatherResponseDTO, CookieUtil.getLoginFromCookie(request.getCookies()))){
            model.addAttribute("isAlreadyAdded",true);
            return "search-results";
        }
        model.addAttribute("weatherResponseDTO", weatherResponseDTO);
        return "search-results";
    }

    private boolean isCityAlreadyAdded(WeatherResponseDTO weatherResponseDTO, String login) {
        return weatherService.isCityAlreadyAdded(login, weatherResponseDTO.getLatitude(),
                weatherResponseDTO.getLongitude());
    }

    @PostMapping("/search")
    public String addLocation(@RequestParam(value = "name", required = true) String name,
                              @RequestParam(value = "latitude", required = true) String latitude,
                              @RequestParam(value = "longitude", required = true) String longitude,
                              Model model) {

        return "redirect:/search-results"; // сделать типа обратно на ту же страницу переводит
    }
}
