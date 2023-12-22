package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.repository.LocationRepository;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.web.dto.location.LocationDto;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.mappers.LocationMapper;
import ru.Bogachev.pet.web.mappers.WeatherMapper;
import ru.Bogachev.pet.service.LocationService;
import ru.Bogachev.pet.service.WeatherApiService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final UserService userService;
    private final WeatherApiService weatherApiService;
    private final WeatherMapper weatherMapper;
    private final LocationMapper locationMapper;


    @Override
    public Map<LocationDto, WeatherDto> getWeatherDataForUserLocations(User user) {
        List<Location> userLocations = locationRepository.findAllLocationsByUserId(user.getId());
        Map<LocationDto, WeatherDto> locationWeatherMap = new LinkedHashMap<>();

        for (Location location : userLocations) {
            WeatherApiResponse weather = getWeatherForLocationUser(location);
            WeatherDto weatherDto = weatherMapper.toDto(weather);
            LocationDto locationDto = locationMapper.toDto(location);
            locationWeatherMap.put(locationDto, weatherDto);
        }
        return locationWeatherMap;
    }

    @Override
    public void addUserLocation(User user, String nameLocation) {
        LocationApiResponse locationResponse = weatherApiService.getLocationByName(nameLocation).get(0);
        if (locationResponse != null) {
            Optional<Location> existingLocation = locationRepository.findByName(locationResponse.getName());
            if (existingLocation.isEmpty()) {
                Location location = new Location();
                location.setName(locationResponse.getName());
                location.setLatitude(locationResponse.getLatitude());
                location.setLongitude(locationResponse.getLongitude());
                locationRepository.save(user, location);
            }
            if (existingLocation.isPresent()) {
            Location location = existingLocation.get();
            Optional<Location> userHasTheLocation = locationRepository.findByUserIdAndLocationName(user.getId(), location.getName());
                if (userHasTheLocation.isEmpty()) {
                    locationRepository.saveUsersLocations(user.getId(), location.getId());
                }
            }
        }
    }

    @Override
    public WeatherApiResponse getWeatherForLocationUser(Location location) {
        return weatherApiService.getWeatherForLocation(location);
    }

    @Override
    public void updateWeatherForLocationUser(Long id) {
        Location location = getLocationById(id);
        weatherApiService.updateWeatherForLocation(location);
    }


    @Override
    public void deleteUserLocation(Long userId, Long locationId) {
        List<User> usersByLocationId = userService.getAllUsersByLocationId(locationId);
        if(usersByLocationId.size() == 1) {
            locationRepository.delete(locationId);
        }
        else {
            locationRepository.deleteUsersLocationsWhereUserIdAndLocationId(userId, locationId);
        }

    }
    private Location getLocationById (Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found."));
    }
}
