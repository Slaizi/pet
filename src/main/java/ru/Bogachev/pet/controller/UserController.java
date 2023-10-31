package ru.Bogachev.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.entity.enums.Role;
import ru.Bogachev.pet.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private static final String USER_LIST_PATH = "/user/list";
    private static final String USER_EDIT_PATH = "/user/edit/{user}";
    private static final String USER_DELETE_PATH = "/user/delete/{user}";
    private final UserService userService;
    @GetMapping(USER_LIST_PATH)
    public String getListUsers (
            @AuthenticationPrincipal UserEntity user, Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("users", userService.findAllUsers());
        return "/main/userList";
    }
    @GetMapping(USER_EDIT_PATH)
    public String getUserForEdit (
            @PathVariable UserEntity user, Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "/main/userEdit";
    }
    @PostMapping(USER_EDIT_PATH)
    public String updateUser (
            @PathVariable UserEntity user,
            @RequestParam String username,
            @RequestParam(value = "roles", required = false) List<String> roles
    ) {
        userService.userEdit(user, username, roles);
        return "redirect:" + USER_LIST_PATH;
    }
    @PostMapping(USER_DELETE_PATH)
    public String deleteUser(
            @PathVariable UserEntity user
    ) {
        userService.deleteUser(user);
        return "redirect:" + USER_LIST_PATH;
    }
}
