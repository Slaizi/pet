package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    @GetMapping
    public String getRegistrationForm () {
        return "/login/registration";
    }
    @PostMapping
    public String registration (UserEntity userEntity, Model model) {
        if(!userService.addUser(userEntity)) {
            model.addAttribute("usernameError", "User exist!");
            return "/login/registration";
        }
        return "redirect:/login";
    }
}
