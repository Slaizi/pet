package ru.Bogachev.pet.service;

import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.user.UserDto;

import java.util.List;

public interface UserService {
    User getById(Long id);

    User getByUsername(String username);

    List<User> getAllUsers();

    List<User> getAllUsersByLocationId(Long id);

    User registerUser(UserDto dto);

    void userEdit(Long id, User user);

    void deleteUser(User user);
}
