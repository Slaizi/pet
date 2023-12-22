package ru.Bogachev.pet.service;

import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.location.Location;

import java.util.List;

public interface WeatherApiService {
    WeatherApiResponse getWeatherForLocation(Location location);
    WeatherApiResponse updateWeatherForLocation(Location location);
    List<LocationApiResponse> getLocationByName(String nameOfLocation);
}
