package ru.Bogachev.pet.service;

import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;

import java.util.Map;

public interface UserService {
    void addUserLocation(UserEntity user, String nameLocation);
    Map<LocationEntity, WeatherDto> getWeatherDataForUserLocations(UserEntity user);
    boolean registerUser(UserEntity user);
    void deleteUserLocation(LocationEntity location);
}
