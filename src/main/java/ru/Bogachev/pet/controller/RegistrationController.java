package ru.Bogachev.pet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.UserService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private static final String REGISTRATION_PATH = "/registration";
    private static final String VIEW_PATCH_REGISTRATION = "/login/registration";
    private final UserService userService;
    @GetMapping(REGISTRATION_PATH)
    public String getRegistrationForm (@ModelAttribute(name = "user") UserEntity user) {
        return VIEW_PATCH_REGISTRATION;
    }
    @PostMapping(REGISTRATION_PATH)
    public String registration (@ModelAttribute(name = "user") @Valid UserEntity userEntity,
                                BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()) {
            return VIEW_PATCH_REGISTRATION;
        }
        if(!userService.registerUser(userEntity)) {
            model.addAttribute("usernameError", "User already exists!");
            return VIEW_PATCH_REGISTRATION;
        }
        return "redirect:/login";
    }
}
