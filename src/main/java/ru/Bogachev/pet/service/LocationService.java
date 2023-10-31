package ru.Bogachev.pet.service;

import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface LocationService {
    Map<LocationEntity, WeatherDto> getWeatherDataForUserLocations(UserEntity user);
    void addUserLocation(UserEntity user, String nameLocation);
    WeatherApiResponse getWeatherForLocationUser(LocationEntity location);
    void updateWeatherForLocationUser(LocationEntity location);
    void deleteUserLocation(LocationEntity location);
}
