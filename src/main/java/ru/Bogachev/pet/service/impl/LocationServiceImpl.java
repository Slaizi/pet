package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(
            value = "LocationService::getById",
            key = "#id"
    )
    @Transactional(readOnly = true)
    public Location getById(final Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found."
                        )
                );
    }

    @Override
    public Map<LocationDto, WeatherDto>
    getWeatherDataForUserLocations(final User user) {
        List<Location> userLocations = locationRepository
                .findAllByUserId(user.getId());
        Map<LocationDto, WeatherDto> locationWeatherMap = new LinkedHashMap<>();

        for (Location location : userLocations) {
            WeatherApiResponse weather = weatherApiService
                    .getWeatherForLocation(location);
            WeatherDto weatherDto = weatherMapper.toDto(weather);
            LocationDto locationDto = locationMapper.toDto(location);
            locationWeatherMap.put(locationDto, weatherDto);
        }
        return locationWeatherMap;
    }

    @Override
    @Transactional
    public void addUserLocation(
            final User user,
            final String nameLocation
    ) {
        LocationApiResponse locationResponse = weatherApiService
                .getLocationByName(nameLocation).get(0);
        if (locationResponse != null) {
            handleLocationResponse(user, locationResponse);
        }
    }

    @Transactional
    private void handleLocationResponse(
            final User user,
            final LocationApiResponse locationResponse
    ) {
        Optional<Location> existingLocation = locationRepository
                .findByName(locationResponse.getName());
        if (existingLocation.isEmpty()) {
            Location location = saveLocation(locationResponse);
            locationRepository
                    .assignLocation(user.getId(), location.getId());
        }
        if (existingLocation.isPresent()) {
            Location location = existingLocation.get();
            handleExistingLocation(user, location);
        }
    }
    @Cacheable(
            value = "LocationService::getById",
            key = "#location.id"
    )
    @Transactional
    private Location saveLocation(final LocationApiResponse locationResponse) {
        Location location = new Location();
        location.setName(locationResponse.getName());
        location.setLatitude(locationResponse.getLatitude());
        location.setLongitude(locationResponse.getLongitude());
        return locationRepository.save(location);
    }

    @Transactional
    private void handleExistingLocation(
            final User user,
            final Location location
    ) {
        boolean userHasTheLocation = locationRepository
                .locationHasUser(user.getId(), location.getId());
        if (!userHasTheLocation) {
            locationRepository
                    .assignLocation(user.getId(), location.getId());
        }
    }

    @Override
    @CacheEvict(
            value = "LocationService::getById",
            key = "#id"
    )
    @Transactional
    public void deleteUserLocation(
            final Long userId,
            final Long id
    ) {
        List<User> usersByLocationId = userService.getAllUsersByLocationId(id);
        if (usersByLocationId.size() == 1) {
            locationRepository.deleteById(id);
        } else {
            locationRepository.removeLocation(userId, id);
        }

    }
}
