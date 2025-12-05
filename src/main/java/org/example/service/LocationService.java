package org.example.service;

import org.example.dao.LocationDao;
import org.example.dao.UserDao;
import org.example.dto.LocationAddResultDTO;
import org.example.dto.LocationError;
import org.example.dto.WeatherResponseDTO;
import org.example.model.Location;
import org.example.model.User;
import org.flywaydb.core.internal.util.Locations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final WeatherService weatherService;
    private final UserDao userDao;
    private final LocationDao locationDao;

    private final static int MAX_LOCATIONS = 4;

    @Autowired
    public LocationService(LocationDao locationDao, WeatherService weatherService, UserDao userDao) {
        this.locationDao = locationDao;
        this.weatherService = weatherService;
        this.userDao = userDao;
    }

    @Transactional
    public LocationAddResultDTO addLocationByLoginAndCityName(String login, String cityName) {
        if(login == null || login.isEmpty() || cityName == null || cityName.isEmpty()){
            return LocationAddResultDTO.failure(LocationError.INVALID_DATA);
        }
        Optional<User> userOpt = userDao.findByLogin(login);
        if(userOpt.isEmpty()){
            return LocationAddResultDTO.failure(LocationError.USER_NOT_FOUND);
        }
        WeatherResponseDTO weatherResponseDTO = weatherService.getWeatherByCityName(cityName);
        if(weatherResponseDTO == null){
            return LocationAddResultDTO.failure(LocationError.CITY_NOT_FOUND);
        }
        if(locationDao.isLocationAlreadyAdded(userOpt.get().getId(), weatherResponseDTO.getLatitude(),
                weatherResponseDTO.getLongitude())){
            return LocationAddResultDTO.failure(LocationError.ALREADY_ADDED);
        }
        Location location = new Location(weatherResponseDTO.getName(), userOpt.get(),
                weatherResponseDTO.getLatitude(), weatherResponseDTO.getLongitude());
        locationDao.save(location);
        return LocationAddResultDTO.success(location);
    }

    public boolean isCityAlreadyAdded(String login,BigDecimal latitude, BigDecimal longitude){
        Optional<User> userOpt =  userDao.findByLogin(login);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            return locationDao.isLocationAlreadyAdded(user.getId(), latitude, longitude);
        }
        return false;
    }

    public boolean isMaxLocationsReached(String login) {
        Optional<User> userOpt =  userDao.findByLogin(login);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            return MAX_LOCATIONS <= locationDao.findAllLocationsById(user.getId()).size();
        }
        return false;
    }

    @Transactional
    public void deleteLocationByCoords(String login, BigDecimal latitude, BigDecimal longitude, String name) {
        Optional<User> userOpt = userDao.findByLogin(login);
        userOpt.ifPresent(user -> locationDao.deleteByUserAndCoords(user.getId(), latitude, longitude));
    }

    public List<WeatherResponseDTO> getAllLocationsByLogin(String login) {
        Optional<User> userOpt = userDao.findByLogin(login);
        if(userOpt.isEmpty()) {
            return Collections.emptyList();
        }
        User user = userOpt.get();

        List<Location> locations = locationDao.findAllLocationsById(user.getId());

        List<WeatherResponseDTO> weatherResponseDTOList = new ArrayList<>();

        for(Location location : locations){
            WeatherResponseDTO weatherResponseDTO = weatherService.
                    getWeatherByCoordinates(location.getLatitude(), location.getLongitude());
            if(weatherResponseDTO != null){
                weatherResponseDTOList.add(weatherResponseDTO);
            }
        }
        return weatherResponseDTOList;
    }
}
