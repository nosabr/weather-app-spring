package org.example.service;

import org.example.dao.LocationDao;
import org.example.dao.UserDao;
import org.example.dto.WeatherResponseDTO;
import org.example.model.Location;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class LocationService {
    private final WeatherService weatherService;
    private final UserDao userDao;
    LocationDao locationDao;

    @Autowired
    public LocationService(LocationDao locationDao, WeatherService weatherService, UserDao userDao) {
        this.locationDao = locationDao;
        this.weatherService = weatherService;
        this.userDao = userDao;
    }

    public boolean addLocationByLoginAndCityName(String login, String cityName) {
        Optional<User> userOpt = userDao.findByLogin(login);
        WeatherResponseDTO weatherResponseDTO = weatherService.getWeatherByCityName(cityName);
        if(userOpt.isEmpty() || weatherResponseDTO == null){
            System.out.println("[LocationService] Location Save unsuccessful");
            return false;
        }

        if(locationDao.isLocationAlreadyAdded(userOpt.get().getId(), weatherResponseDTO.getLatitude(),
                weatherResponseDTO.getLongitude())){
            System.out.println("[LocationService] Location Already added");
            return false;
        }

        Location location = new Location(weatherResponseDTO.getName(), userOpt.get(),
                weatherResponseDTO.getLatitude(), weatherResponseDTO.getLongitude());
        locationDao.save(location);
        System.out.println("[LocationService] Location Saved Successfully");
        return true;
    }

    public boolean isCityAlreadyAdded(String login,BigDecimal latitude, BigDecimal longitude){
        Optional<User> userOpt =  userDao.findByLogin(login);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            return locationDao.isLocationAlreadyAdded(user.getId(), latitude, longitude);
        }
        return false;
    }
}
