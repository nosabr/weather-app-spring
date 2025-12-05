package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.WeatherResponseDTO;
import org.example.service.LocationService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {
    private final LocationService locationService;

    @Autowired
    public IndexController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String login = CookieUtil.getLoginFromCookie(request.getCookies());

        List<WeatherResponseDTO> locations = locationService.getAllLocationsByLogin(login);
        model.addAttribute("locations", locations);
        return "index";
    }
}
