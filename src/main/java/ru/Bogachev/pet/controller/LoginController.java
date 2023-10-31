package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.Bogachev.pet.domain.entity.UserEntity;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private static final String LOGIN_PATH = "/login";
    private final AuthenticationManager authenticationManager;
    @GetMapping(LOGIN_PATH)
    public String login(@ModelAttribute("user") UserEntity user,
                        @RequestParam(name = "error", required = false) String error,
                        Model model) {
        if(error != null) model.addAttribute("errorMessage", "Authentication error: Invalid username or password");
        return "/login" + LOGIN_PATH;
    }
    @PostMapping(LOGIN_PATH)
    public String login(UserEntity user) {
         try {
             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
             return "/main/main";
         } catch (BadCredentialsException e) {
             return "redirect:" + LOGIN_PATH;
         }

    }
}
