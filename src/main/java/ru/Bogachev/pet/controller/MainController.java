package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.LocationService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private static final String BASE_PATH = "/";
    private static final String SEARCH_PATH = "/search";
    private final LocationService locationService;
    @GetMapping(BASE_PATH)
    public String mainPage(@AuthenticationPrincipal UserEntity user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("locationWeatherMap", locationService.getWeatherDataForUserLocations(user));
        return "/main/main";
    }
    @PostMapping(BASE_PATH)
    public String deleteLocation (@RequestParam(name = "locationId") LocationEntity location) {
        locationService.deleteUserLocation(location);
        return "redirect:" + BASE_PATH;
    }
    @PostMapping("/update")
    public String updateLocation(@RequestParam(name = "locationId") LocationEntity location) {
        locationService.updateWeatherForLocationUser(location);
        return "redirect:" + BASE_PATH;
    }
    @GetMapping(SEARCH_PATH)
    public String cardWeather (@AuthenticationPrincipal UserEntity user,
                               @RequestParam(name = "search", required = false) String search) {
        if(!search.isEmpty() && !search.matches(".*\\d.*")) {
            locationService.addUserLocation(user, search);
        }
        return "redirect:" + BASE_PATH;
    }
}
