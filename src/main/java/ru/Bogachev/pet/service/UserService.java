package ru.Bogachev.pet.service;

import ru.Bogachev.pet.domain.dto.WeatherDto;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.entity.enums.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {
    boolean registerUser(UserEntity user);
    List<UserEntity> findAllUsers();
    void userEdit(UserEntity user, String username, List<String> roles);
    void deleteUser(UserEntity user);
}
