package ru.Bogachev.pet.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.domain.user.Role;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.web.dto.user.UpdateUserDto;
import ru.Bogachev.pet.web.dto.user.UserDetailsDto;
import ru.Bogachev.pet.web.dto.user.UserDto;
import ru.Bogachev.pet.web.mappers.UserDetailsMapper;
import ru.Bogachev.pet.web.mappers.UserMapper;
import ru.Bogachev.pet.web.security.UserDetails;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserDetailsMapper userDetailsMapper;

    private static final String USER_EDIT_PATH = "/edit/{id}";
    private static final String USER_EDIT_PAGE = "main/userEdit";
    private static final String USER_LIST_PAGE = "main/userList";
    private static final String REDIRECT_USERS = "redirect:/users";

    @GetMapping
    @PreAuthorize("@securityExpression.canAccessUser()")
    public String getUserList(
            @AuthenticationPrincipal UserDetails userDetails, Model model
    ) {
        UserDetailsDto userDto = userDetailsMapper.toDto(userDetails);
        List<UserDto> userDtoList = userMapper.toDto(userService.getAllUsers());
        model.addAttribute("user", userDto);
        model.addAttribute("users", userDtoList);
        return USER_LIST_PAGE;
    }
    @GetMapping(USER_EDIT_PATH)
    @PreAuthorize("@securityExpression.canAccessUser()")
    public String getUserFormForEdit(
            @PathVariable Long id,
            Model model
    ) {
        User user = userService.getUserById(id);
        model.addAttribute("user", userMapper.toDto(user));
        model.addAttribute("roles", Role.values());
        return USER_EDIT_PAGE;
    }

    @PutMapping(USER_EDIT_PATH)
    @PreAuthorize("@securityExpression.canAccessUser()")
    public String updateUser (
            @PathVariable Long id,
            @Valid UpdateUserDto updateUserDto,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            Map<String, String> mapErrors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(mapErrors);
            model.addAttribute("user", updateUserDto);
            model.addAttribute("roles", Role.values());
            return USER_EDIT_PAGE;
        }
        userService.userEdit(id, updateUserDto);
        return REDIRECT_USERS;
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("@securityExpression.canAccessUser()")
    public String deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return REDIRECT_USERS;
    }
}
