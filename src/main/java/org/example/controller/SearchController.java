package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.UserDao;
import org.example.dto.WeatherResponseDTO;
import org.example.model.User;
import org.example.service.LocationService;
import org.example.service.WeatherService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class SearchController {
    private final LocationService locationService;
    private final UserDao userDao;
    WeatherService weatherService;

    @Autowired
    public SearchController(WeatherService weatherService, LocationService locationService, UserDao userDao) {
        this.weatherService = weatherService;
        this.locationService = locationService;
        this.userDao = userDao;
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

        String login = CookieUtil.getLoginFromCookie(request.getCookies());
        BigDecimal latitude = weatherResponseDTO.getLatitude();
        BigDecimal longitude = weatherResponseDTO.getLongitude();

        if(locationService.isCityAlreadyAdded(login, latitude, longitude)){
            model.addAttribute("isAlreadyAdded",true);
        }
        model.addAttribute("weatherResponseDTO", weatherResponseDTO);
        return "search-results";
    }

    @PostMapping("/search")
    public String addLocation(@RequestParam(value = "cityName", required = true) String cityName,
                              Model model,
                              HttpServletRequest request) {
        String login = CookieUtil.getLoginFromCookie(request.getCookies());
        boolean success = locationService.addLocationByLoginAndCityName(login,cityName);
        if(success){
            model.addAttribute("success", "Город успешно добавлен!");
            model.addAttribute("isAlreadyAdded", true);
            return "redirect:/search?cityName=" + cityName;
        }
        model.addAttribute("error", "Ошибка добавления города");
        return "redirect:/search"; //
    }
}
