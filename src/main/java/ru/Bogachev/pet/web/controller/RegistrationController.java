package ru.Bogachev.pet.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.web.dto.user.UserDto;
import ru.Bogachev.pet.web.mappers.UserMapper;

import java.util.Map;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private static final String REGISTRATION_PAGE = "/login/registration";

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public String getRegistrationForm (@ModelAttribute(name = "user") UserDto userDto) {
        return REGISTRATION_PAGE;
    }
    @PostMapping
    public String registration (@ModelAttribute(name = "user") @Valid UserDto userDto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            return REGISTRATION_PAGE;
        }
        User user = userMapper.toEntity(userDto);
        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return REGISTRATION_PAGE;
        }
    }
}
