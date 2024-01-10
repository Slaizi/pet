package ru.Bogachev.pet.service;

import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.location.LocationDto;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;
import java.util.Map;

public interface LocationService {
    Map<LocationDto, WeatherDto> getWeatherDataForUserLocations(User user);
    void addUserLocation(User user, String nameLocation);
    void updateWeatherForLocationUser(Long id);
    void deleteUserLocation(Long userId, Long id);
}
