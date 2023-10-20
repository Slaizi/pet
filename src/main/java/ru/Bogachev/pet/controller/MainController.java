package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.impl.UserServiceImpl;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final UserServiceImpl userServiceImpl;
    @GetMapping
    public String mainPage(@AuthenticationPrincipal UserEntity user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("locationWeatherMap", userServiceImpl.getWeatherDataForUserLocations(user));
        return "/main/main";
    }
    @GetMapping("login")
    public String login(@ModelAttribute("user") UserEntity user,
                        @RequestParam(name = "error", required = false) String error,
                        Model model) {
        if(error != null) model.addAttribute("errorMessage", "Authentication error: Invalid username or password");
        return "/login/login";
    }
    @PostMapping
    public String deleteLocation (@RequestParam(name = "locationId") LocationEntity location) {
        userServiceImpl.deleteUserLocation(location);
        return "redirect:/";
    }
    @GetMapping("search")
    public String cardWeather (@AuthenticationPrincipal UserEntity user,
                               @RequestParam(name = "search", required = false) String search,
                               Model model) {
        userServiceImpl.addUserLocation(user, search);
        return "redirect:/";
    }
}
