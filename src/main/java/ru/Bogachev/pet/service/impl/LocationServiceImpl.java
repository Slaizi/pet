package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.api.service.WeatherApiService;
import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.mappers.WeatherMapper;
import ru.Bogachev.pet.domain.repository.LocationRepository;
import ru.Bogachev.pet.service.LocationService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final WeatherApiService weatherApiService;
    private final WeatherMapper weatherMapper;


    @Override
    public Map<LocationEntity, WeatherDto> getWeatherDataForUserLocations(UserEntity user) {
        List<LocationEntity> userLocations = locationRepository.findByUserId(user.getId());
        Map<LocationEntity, WeatherDto> locationWeatherMap = new LinkedHashMap<>();

        for (LocationEntity location : userLocations) {
            WeatherApiResponse weather = getWeatherForLocationUser(location);
            WeatherDto weatherDto = weatherMapper.toDto(weather);
            locationWeatherMap.put(location, weatherDto);
        }
        return locationWeatherMap;
    }

    @Override
    public void addUserLocation(UserEntity user, String nameLocation) {
        Optional<LocationEntity> existingLocation = locationRepository.findByNameAndUserId(nameLocation, user.getId());
        if (existingLocation.isEmpty()) {
            List<LocationApiResponse> listLocationResponse = weatherApiService.getLocationByName(nameLocation);
            listLocationResponse.stream().map(e -> LocationEntity.builder()
                                                                 .name(e.getName())
                                                                 .latitude(e.getLatitude())
                                                                 .longitude(e.getLongitude())
                                                                 .user(user)
                                                                 .build()).findFirst().ifPresent(locationRepository:: save);
        }
    }

    @Override
    public WeatherApiResponse getWeatherForLocationUser(LocationEntity location) {
        return weatherApiService.getWeatherForLocationAndCached(location);
    }

    @Override
    public void updateWeatherForLocationUser(LocationEntity location) {
        weatherApiService.updateWeatherForLocationAndCache(location);
    }


    @Override
    @CacheEvict(value = "WeatherApiService::getWeatherForLocation", key = "#location.id")
    public void deleteUserLocation(LocationEntity location) {
        locationRepository.delete(location);
    }
}
