package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.service.impl.UserServiceImpl;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserServiceImpl userServiceImpl;
    @GetMapping
    public String getRegistrationForm (@ModelAttribute("user") UserEntity user) {
        return "/login/registration";
    }
    @PostMapping
    public String registration (@ModelAttribute("user") @Valid UserEntity userEntity,
                                BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()) {
            return "/login/registration";
        }
        if(!userServiceImpl.registerUser(userEntity)) {
            model.addAttribute("usernameError", "User already exists!");
            return "/login/registration";
        }
        return "redirect:/login";
    }
}
