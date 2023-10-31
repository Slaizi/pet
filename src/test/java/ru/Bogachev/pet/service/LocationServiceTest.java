package ru.Bogachev.pet.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.api.service.WeatherApiService;
import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.mappers.WeatherMapper;
import ru.Bogachev.pet.domain.repository.LocationRepository;
import ru.Bogachev.pet.service.impl.LocationServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @Mock
    private WeatherApiService weatherApiService;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private WeatherMapper weatherMapper;
    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    public void testAddUserLocation_NonExistingLocation_Success() {
        UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);
        String nameLocation = "London";


        when(locationRepository.findByNameAndUserId(anyString(), anyLong())).thenReturn(Optional.empty());
        LocationApiResponse locationApiResponse = new LocationApiResponse(nameLocation, 51.5073219, -0.1276474, "England");
        when(weatherApiService.getLocationByName(nameLocation)).thenReturn(List.of(locationApiResponse));

        locationService.addUserLocation(user, nameLocation);

        verify(locationRepository, times(1)).findByNameAndUserId(nameLocation, user.getId());
        verify(locationRepository, times(1)).save(any());
    }
    @Test
    public void testAddUserLocation_ExistingLocation_NoSave() {
        UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);
        String nameLocation = "London";
        LocationEntity location = new LocationEntity(1L,nameLocation,51.5073219, -0.1276474, user);

        when(locationRepository.findByNameAndUserId(anyString(), anyLong())).thenReturn(Optional.of(location));
        locationService.addUserLocation(user, nameLocation);

        verify(locationRepository, times(1)).findByNameAndUserId(nameLocation, user.getId());
        verify(locationRepository, times(0)).save(any());
    }

    @Test
    public void testGetWeatherDataForUserLocations () {
        UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);
        LocationEntity location = new LocationEntity(2L, "New York", 40.7128, -74.0060, user);

        when(locationRepository.findByUserId(user.getId())).thenReturn(List.of(location));
        WeatherApiResponse weather = mock(WeatherApiResponse.class);
        when(weatherApiService.getWeatherForLocationAndCached(location)).thenReturn(weather);
        var dto = WeatherDto.builder().build();
        when(weatherMapper.toDto(weather)).thenReturn(dto);

        Map<LocationEntity, WeatherDto> result = locationService.getWeatherDataForUserLocations(user);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(location));
        assertEquals(dto, result.get(location));

        verify(locationRepository, times(1)).findByUserId(user.getId());
        verify(weatherApiService, times(1)).getWeatherForLocationAndCached(location);
        verify(weatherMapper, times(1)).toDto(weather);

    }
    @Test
    public void testDeleteUserLocation () {
        LocationEntity location = mock(LocationEntity.class);
        locationService.deleteUserLocation(location);

        verify(locationRepository, times(1)).delete(location);
    }
}