package ru.Bogachev.pet.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private static final String MAIN_PAGE = "main/main";

    private final LocationService locationService;
    private final UserDetailsMapper userDetailsMapper;
    private final UserService userService;

    @GetMapping
    public String getMainPage(
            @AuthenticationPrincipal final UserDetails userDetails,
            final Model model
    ) {
        UserDetailsDto userDto = userDetailsMapper.toDto(userDetails);
        model.addAttribute("user", userDto);
        User user = userService.getById(userDetails.getId());
        model.addAttribute("locationWeatherMap",
                locationService.getWeatherDataForUserLocations(user));
        return MAIN_PAGE;
    }

    @DeleteMapping
    public String deleteLocation(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestParam(name = "locationId") final Long locationId
    ) {
        User user = userService.getById(userDetails.getId());
        locationService.deleteUserLocation(user.getId(), locationId);
        return REDIRECT_BASE_PATH;
    }

    @PostMapping
    public String cardWeather(
            @AuthenticationPrincipal final UserDetails userDetails,
            @RequestParam(name = "search", required = false) final String search
    ) {
        if (!search.isEmpty() && !search.matches(".*\\d.*")) {
            User user = userService.getById(userDetails.getId());
            locationService.addUserLocation(user, search);
        }
        return REDIRECT_BASE_PATH;
    }
}
