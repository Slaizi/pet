package ru.Bogachev.pet.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private MainController mainController;

    @Test
    public void testMainPage () {
        UserEntity user = new UserEntity();
        Map<LocationEntity, WeatherDto> locationWeatherMap = new HashMap<>();

        Model model = new ExtendedModelMap();

        when(userService.getWeatherDataForUserLocations(any(UserEntity.class))).thenReturn(locationWeatherMap);

        String result = mainController.mainPage(user, model);

        assertEquals("/main/main", result);
        assertEquals(user, model.getAttribute("user"));
        assertEquals(locationWeatherMap, model.getAttribute("locationWeatherMap"));

        verify(userService, times(1)).getWeatherDataForUserLocations(user);
    }
    @Test
    public void testDeleteLocation () {
        LocationEntity location = mock(LocationEntity.class);
        String expectedPath = "redirect:/";

        String result = mainController.deleteLocation(location);

        verify(userService,times(1)).deleteUserLocation(location);
        assertEquals(expectedPath, result);
    }
    @Test
    public void testCardWeather() {
        String expectedPath = "redirect:/";
        UserEntity user = new UserEntity();
        String locationName = "London";

        String result = mainController.cardWeather(user, locationName);
        verify(userService, times(1)).addUserLocation(user, locationName);

        assertEquals(expectedPath, result);
    }
}
