package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.UserService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    @GetMapping("/")
    public String hello (@AuthenticationPrincipal UserEntity user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("locationWeatherMap", userService.getWeatherDataForUserLocations(user));
        return "/main/main";
    }
    @GetMapping("/search")
    public String cardWeather (@AuthenticationPrincipal UserEntity user,
                               @RequestParam(name = "search", required = false) String search,
                               Model model) {
        userService.saveUserLocation(user, search);
        return "redirect:/";
    }
}
