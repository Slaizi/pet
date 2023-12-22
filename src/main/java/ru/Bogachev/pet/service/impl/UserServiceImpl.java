package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.domain.user.Role;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.repository.UserRepository;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.web.dto.user.UpdateUserDto;
import ru.Bogachev.pet.web.dto.user.UserDto;
import ru.Bogachev.pet.web.mappers.UserMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User not found with username %s", username)
                ));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsersByLocationId(Long id) {
        return userRepository.findAllUsersByLocationId(id);
    }

    @Override
    public void registerUser(User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResourceNotFoundException("User already exist!");
        }
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void userEdit(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.setUsername(updateUserDto.getUsername());

        List<String> roles = updateUserDto.getRoles();
        if(roles.isEmpty()) {
            roles.add("ROLE_USER");
        }
        Set<Role> setRoles = roles.stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());

        user.setRoles(setRoles);
        userRepository.update(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }


}
