package ru.Bogachev.pet.service;

import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.location.LocationDto;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;
import java.util.Map;

public interface LocationService {
    Map<LocationDto, WeatherDto> getWeatherDataForUserLocations(User user);
    void addUserLocation(User user, String nameLocation);
    Location getById(Long id);
    void deleteLocation(Long userId, Long id);
}
