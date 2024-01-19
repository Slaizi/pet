package ru.Bogachev.pet.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.config.TestConfig;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.repository.LocationRepository;
import ru.Bogachev.pet.service.impl.LocationServiceImpl;
import ru.Bogachev.pet.web.dto.location.LocationDto;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;
import ru.Bogachev.pet.web.mappers.LocationMapper;
import ru.Bogachev.pet.web.mappers.WeatherMapper;

import java.util.*;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {

    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private WeatherApiService weatherApiService;
    @MockBean
    private LocationMapper locationMapper;
    @MockBean
    private WeatherMapper weatherMapper;

    @Autowired
    private LocationServiceImpl locationService;


    @Test
    void getById() {
        Long id = 1L;
        Location location = new Location();
        location.setId(id);
        Mockito.when(locationRepository.findById(id))
                .thenReturn(Optional.of(location));
        Location testLocation = locationService.getById(id);
        verify(locationRepository).findById(id);
        Assertions.assertEquals(location, testLocation);
    }

    @Test
    void getByIdWithNotExistingLocation() {
        Long id = 1L;
        Mockito.when(locationRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> locationService.getById(id)
        );
        verify(locationRepository).findById(id);
    }

    @Test
    void getWeatherDataForUserLocations() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Location location = new Location();
        location.setId(id);
        location.setName("name");
        WeatherApiResponse weather = new WeatherApiResponse();

        Mockito.when(locationRepository.findAllByUserId(id))
                .thenReturn(List.of(location));
        Mockito.when(weatherApiService.getWeatherForLocation(location))
                .thenReturn(weather);

        Map<LocationDto, WeatherDto> map = new LinkedHashMap<>();
        map.put(
                locationMapper.toDto(location),
                weatherMapper.toDto(weather)
        );
        Map<LocationDto, WeatherDto> result = locationService.getWeatherDataForUserLocations(user);

        Assertions.assertEquals(map, result);
        Mockito.verify(weatherApiService, Mockito.times(result.size()))
                .getWeatherForLocation(location);
    }

    @Test
    @SneakyThrows
    void addUserLocation() {
        String nameLocation = "location";
        Long id = 1L;

        User user = new User();
        user.setId(id);

        Location savedLocation = new Location();
        savedLocation.setId(id);

        Mockito.when(weatherApiService.getLocationByName(nameLocation))
                .thenReturn(Collections.singletonList(new LocationApiResponse()));
        Mockito.when(locationRepository.findByName(nameLocation))
                .thenReturn(Optional.empty());
        Mockito.when(locationRepository.save(Mockito.any(Location.class)))
                .thenReturn(savedLocation);

        locationService.addUserLocation(user, nameLocation);

        Mockito.verify(locationRepository)
                .save(Mockito.any(Location.class));
        Mockito.verify(locationRepository).locationHasUser(
                user.getId(), savedLocation.getId());
        Mockito.verify(locationRepository)
                .assignLocation(user.getId(), savedLocation.getId());
    }

    @Test
    void addLocationWithException() {
        String nameLocation = "location";
        User user = new User();
        Mockito.when(weatherApiService.getLocationByName(nameLocation))
                .thenReturn(Collections.emptyList());
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> locationService.addUserLocation(user, nameLocation)
        );
    }

    @Test
    void deleteLocation() {
        Long id = 1L;
        Mockito.when(userService.getAllUsersByLocationId(id))
                .thenReturn(List.of(new User()));
        locationService.deleteLocation(id, id);
        Mockito.verify(locationRepository).deleteById(id);
        Mockito.verify(locationRepository, Mockito.times(0))
                .unlinkLocation(id, id);
    }

    @Test
    void deleteUnlinkLocation() {
        Long id = 1L;
        Mockito.when(userService.getAllUsersByLocationId(id))
                .thenReturn(Collections.emptyList());
        locationService.deleteLocation(id, id);
        Mockito.verify(locationRepository, Mockito.times(0))
                .deleteById(id);
        Mockito.verify(locationRepository)
                .unlinkLocation(id, id);
    }
}
