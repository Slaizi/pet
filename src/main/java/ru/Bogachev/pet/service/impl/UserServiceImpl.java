package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.api.service.WeatherApiService;
import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.entity.enums.Role;
import ru.Bogachev.pet.domain.mappers.WeatherMapper;
import ru.Bogachev.pet.domain.repository.LocationRepository;
import ru.Bogachev.pet.domain.repository.UserRepository;
import ru.Bogachev.pet.service.UserService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WeatherApiService weatherApiService;
    private final LocationRepository locationRepository;
    private final WeatherMapper weatherMapper;

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
                                                                 .build()).findFirst().ifPresent(locationRepository::save);
        }
    }

    @Override
    public Map<LocationEntity, WeatherDto> getWeatherDataForUserLocations(UserEntity user) {
        List<LocationEntity> userLocation = locationRepository.findByUserId(user.getId());
        Map<LocationEntity, WeatherDto> locationWeatherMap = new LinkedHashMap<>();

        for (LocationEntity location : userLocation) {
            WeatherApiResponse weather = weatherApiService.getWeatherForLocation(location);
            WeatherDto weatherDto = weatherMapper.toDto(weather);
            locationWeatherMap.put(location, weatherDto);
        }
        return locationWeatherMap;
    }

    @Override
    public boolean registerUser(UserEntity user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    @Override
    public void deleteUserLocation(LocationEntity location) {
        locationRepository.delete(location);
    }

}
