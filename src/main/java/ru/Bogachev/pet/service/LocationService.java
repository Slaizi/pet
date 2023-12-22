package ru.Bogachev.pet.service;

import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.web.dto.location.LocationDto;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.User;

import java.util.Map;

public interface LocationService {
    Map<LocationDto, WeatherDto> getWeatherDataForUserLocations(User user);
    void addUserLocation(User user, String nameLocation);
    WeatherApiResponse getWeatherForLocationUser(Location location);
    void updateWeatherForLocationUser(Long id);
    void deleteUserLocation(Long userId, Long id);
}
