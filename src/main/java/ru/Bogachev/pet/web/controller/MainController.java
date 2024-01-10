package ru.Bogachev.pet.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.service.LocationService;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.web.dto.user.UserDetailsDto;
import ru.Bogachev.pet.web.mappers.UserDetailsMapper;
import ru.Bogachev.pet.web.security.UserDetails;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private static final String REDIRECT_BASE_PATH = "redirect:/";

    private final LocationService locationService;
    private final UserDetailsMapper userDetailsMapper;
    private final UserService userService;

    @GetMapping
    public String getMainPage(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        UserDetailsDto userDto = userDetailsMapper.toDto(userDetails);
        model.addAttribute("user", userDto);
        User user = userService.getUserById(userDetails.getId());
        model.addAttribute("locationWeatherMap", locationService.getWeatherDataForUserLocations(user));
        return "/main/main";
    }

    @DeleteMapping
    public String deleteLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "locationId") Long locationId
    ) {
        User user = userService.getUserById(userDetails.getId());
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
    public String cardWeather(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "search", required = false) String search
    ) {
        if (!search.isEmpty() && !search.matches(".*\\d.*")) {
            User user = userService.getUserById(userDetails.getId());
            locationService.addUserLocation(user, search);
        }
        return REDIRECT_BASE_PATH;
    }
}
