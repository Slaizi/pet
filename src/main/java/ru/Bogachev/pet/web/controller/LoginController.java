package ru.Bogachev.pet.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.Bogachev.pet.web.dto.user.UserDto;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private static final String LOGIN_PAGE = "login/login";

    @GetMapping
    public String login(
            @ModelAttribute("user") final UserDto userDto,
            @RequestParam(name = "error", required = false) final String error,
            final Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        return LOGIN_PAGE;
    }
}
