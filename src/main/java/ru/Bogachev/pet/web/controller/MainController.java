package ru.Bogachev.pet.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.service.LocationService;
import ru.Bogachev.pet.web.dto.user.UserDto;
import ru.Bogachev.pet.web.mappers.UserMapper;
import ru.Bogachev.pet.web.security.UserDetails;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private static final String REDIRECT_BASE_PATH = "redirect:/";

    private final LocationService locationService;
    private final UserMapper userMapper;
    @GetMapping
    public String getMainPage(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        UserDto userDto = userMapper.toDtoWhereUserDetails(userDetails);
        model.addAttribute("user", userDto);
        User user = userMapper.toEntityWhereUserService(userDetails);
        model.addAttribute("locationWeatherMap", locationService.getWeatherDataForUserLocations(user));
        return "/main/main";
    }
    @DeleteMapping
    public String deleteLocation (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "locationId") Long locationId
    ) {
        User user = userMapper.toEntityWhereUserService(userDetails);
        locationService.deleteUserLocation(user.getId(), locationId);
        return REDIRECT_BASE_PATH;
    }
    @PutMapping
    public String updateLocation(
            @RequestParam(name = "locationId") Long locationId
    ) {
        locationService.updateWeatherForLocationUser(locationId);
        return REDIRECT_BASE_PATH;
    }
    @PostMapping
    public String cardWeather (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "search", required = false) String search
    ) {
        if(!search.isEmpty() && !search.matches(".*\\d.*")) {
            User user = userMapper.toEntityWhereUserService(userDetails);
            locationService.addUserLocation(user, search);
        }
        return REDIRECT_BASE_PATH;
    }
}
