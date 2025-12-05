package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.UserDao;
import org.example.dto.LocationAddResultDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class SearchController {
    private final LocationService locationService;
    WeatherService weatherService;

    @Autowired
    public SearchController(WeatherService weatherService, LocationService locationService) {
        this.weatherService = weatherService;
        this.locationService = locationService;
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

        boolean isAlreadyAdded = locationService.isCityAlreadyAdded(login, latitude, longitude);
        boolean maxLocationsReached = locationService.isMaxLocationsReached(login);

        model.addAttribute("isAlreadyAdded", isAlreadyAdded);
        model.addAttribute("maxLocationsReached", maxLocationsReached);
        model.addAttribute("weatherResponseDTO", weatherResponseDTO);
        return "search-results";
    }

    @PostMapping("/search/add")
    public String addLocation(@RequestParam(value = "cityName", required = true) String cityName,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        String login = CookieUtil.getLoginFromCookie(request.getCookies());

        LocationAddResultDTO locationAddResultDTO = locationService.addLocationByLoginAndCityName(login,cityName);
        if(locationAddResultDTO.isSuccess()){
            redirectAttributes.addFlashAttribute("success", "Город успешно добавлен!");
            redirectAttributes.addAttribute("cityName", cityName);
            return "redirect:/search";
        }
        redirectAttributes.addFlashAttribute("error", locationAddResultDTO.getError());
        return "redirect:/search"; //
    }


    @PostMapping("/search/delete")
    public String deleteLocation(@RequestParam(value = "cityName", required = true) String cityName,
                                 @RequestParam(value = "name", required = true) String name,
                                 @RequestParam(value = "latitude", required = true) BigDecimal latitude,
                                 @RequestParam(value = "longitude", required = true) BigDecimal longitude,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {

        try{
            String login = CookieUtil.getLoginFromCookie(request.getCookies());
            locationService.deleteLocationByCoords(login, latitude, longitude, name);
            redirectAttributes.addFlashAttribute("success","Город успешно удален");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
        }
        redirectAttributes.addAttribute("cityName", cityName);
        return "redirect:/search";
    }
}
