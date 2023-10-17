package ru.Bogachev.pet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.api.entity.Weather;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.enums.Role;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.entity.enums.TimeOfDay;
import ru.Bogachev.pet.domain.entity.enums.WeatherCondition;
import ru.Bogachev.pet.domain.repository.LocationRepository;
import ru.Bogachev.pet.domain.repository.UserRepository;
import ru.Bogachev.pet.service.api.WeatherApiService;

import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WeatherApiService weatherApiService;
    private final LocationRepository locationRepository;

    public void saveUserLocation (UserEntity user, String nameLocation) {
        List<LocationApiResponse> listLocationResponse = weatherApiService.getLocationByName(nameLocation);
        LocationEntity locationEntity = listLocationResponse.stream().map(e -> LocationEntity.builder()
                                                         .name(e.getName())
                                                         .latitude(e.getLatitude())
                                                         .longitude(e.getLongitude())
                                                         .user(user)
                                                         .build()).toList().get(0);
        locationRepository.save(locationEntity);
    }
    public Map<LocationEntity, WeatherDto> getWeatherDataForUserLocations(UserEntity user) {
        List<LocationEntity> userLocation = locationRepository.findByUserId(user.getId());
        Map<LocationEntity, WeatherDto> locationWeatherMap = new HashMap<>();

        for (LocationEntity location : userLocation) {
            WeatherApiResponse weather = weatherApiService.getWeatherForLocation(location);
            WeatherDto weatherDto = buildWeatherDto(weather);
            locationWeatherMap.put(location, weatherDto);
        }
        return locationWeatherMap;
    }

    public boolean addUser (UserEntity user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    private WeatherDto buildWeatherDto(WeatherApiResponse weatherApiResponse) {
        Weather weather = weatherApiResponse.getWeatherList().get(0);
        return WeatherDto.builder()
                         .weatherCondition(WeatherCondition.getWeatherConditionForCode(weather.getId()))
                         .timeOfDay(TimeOfDay.getTimeOfDayForTime(weatherApiResponse.getDate()))
                         .description(weather.getDescription())
                         .temperature(weatherApiResponse.getMain().getTemperature())
                         .temperatureFeelsLike(weatherApiResponse.getMain().getTemperatureFeelsLike())
                         .temperatureMinimum(weatherApiResponse.getMain().getTemperatureMinimal())
                         .temperatureMaximum(weatherApiResponse.getMain().getTemperatureMaximum())
                         .humidity(weatherApiResponse.getMain().getHumidity())
                         .pressure(weatherApiResponse.getMain().getPressure())
                         .windSpeed(weatherApiResponse.getWind().getSpeed())
                         .windDirection(weatherApiResponse.getWind().getDeg())
                         .windGust(weatherApiResponse.getWind().getGust())
                         .cloudiness(weatherApiResponse.getClouds().getCloudiness())
                         .date(Date.from(weatherApiResponse.getDate().atZone(ZoneId.systemDefault()).toInstant()))
                         .sunrise(Date.from(weatherApiResponse.getSys().getSunriseTime().atZone(ZoneId.systemDefault()).toInstant()))
                         .sunset(Date.from(weatherApiResponse.getSys().getSunsetTime().atZone(ZoneId.systemDefault()).toInstant()))
                         .build();
    }
}
