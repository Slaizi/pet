package ru.Bogachev.pet.service;

import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.user.UpdateUserDto;
import ru.Bogachev.pet.web.dto.user.UserDto;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<UserDto> getAllUsers();
    List<User> getAllUsersByLocationId(Long id);
    void registerUser(User user);
    void userEdit(Long id, UpdateUserDto updateUserDto);
    void deleteUser(Long id);
}
