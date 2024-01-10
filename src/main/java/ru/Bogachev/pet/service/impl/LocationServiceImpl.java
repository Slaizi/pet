package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.repository.LocationRepository;
import ru.Bogachev.pet.service.LocationService;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.service.WeatherApiService;
import ru.Bogachev.pet.web.dto.location.LocationDto;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;
import ru.Bogachev.pet.web.mappers.LocationMapper;
import ru.Bogachev.pet.web.mappers.WeatherMapper;

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
        List<Location> userLocations = locationRepository.findAllByUserId(user.getId());
        Map<LocationDto, WeatherDto> locationWeatherMap = new LinkedHashMap<>();

        for (Location location : userLocations) {
            WeatherApiResponse weather = weatherApiService.getWeatherForLocation(location);
            WeatherDto weatherDto = weatherMapper.toDto(weather);
            LocationDto locationDto = locationMapper.toDto(location);
            locationWeatherMap.put(locationDto, weatherDto);
        }
        return locationWeatherMap;
    }
    @Transactional(readOnly = true)
    private Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found."));
    }

    @Override
    @Transactional
    public void addUserLocation(User user, String nameLocation) {
        LocationApiResponse locationResponse = weatherApiService.getLocationByName(nameLocation).get(0);
        if (locationResponse != null) {
            Optional<Location> existingLocation = locationRepository.findByName(locationResponse.getName());
            if (existingLocation.isEmpty()) {
                Location location = new Location();
                location.setName(locationResponse.getName());
                location.setLatitude(locationResponse.getLatitude());
                location.setLongitude(locationResponse.getLongitude());
                locationRepository.save(location);
                locationRepository.assignLocation(user.getId(), location.getId());
            }
            if (existingLocation.isPresent()) {
                Location location = existingLocation.get();
                boolean userHasTheLocation = locationRepository.locationHasUser(user.getId(), location.getId());
                if (!userHasTheLocation) {
                    locationRepository.assignLocation(user.getId(), location.getId());
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void updateWeatherForLocationUser(Long id) {
        Location location = getLocationById(id);
        weatherApiService.updateWeatherForLocation(location);
    }


    @Override
    @Transactional
    public void deleteUserLocation(Long userId, Long locationId) {
        List<User> usersByLocationId = userService.getAllUsersByLocationId(locationId);
        if (usersByLocationId.size() == 1) {
            locationRepository.deleteById(locationId);
        } else {
            locationRepository.removeLocation(userId, locationId);
        }

    }
}
