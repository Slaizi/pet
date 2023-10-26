package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private static final String REGISTRATION_PATH = "/registration";
    private static final String LOGIN_PATH = "/login";
    private final UserService userService;
    @GetMapping(REGISTRATION_PATH)
    public String getRegistrationForm (@ModelAttribute(name = "user") UserEntity user) {
        return "/login/registration";
    }
    @PostMapping(REGISTRATION_PATH)
    public String registration (@ModelAttribute(name = "user") @Valid UserEntity userEntity,
                                BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()) {
            return "/login/registration";
        }
        if(!userService.registerUser(userEntity)) {
            model.addAttribute("usernameError", "User already exists!");
            return "/login/registration";
        }
        return "redirect:" + LOGIN_PATH;
    }
    @GetMapping(LOGIN_PATH)
    public String login(@ModelAttribute("user") UserEntity user,
                        @RequestParam(name = "error", required = false) String error,
                        Model model) {
        if(error != null) model.addAttribute("errorMessage", "Authentication error: Invalid username or password");
        return "/login/login";
    }
}
