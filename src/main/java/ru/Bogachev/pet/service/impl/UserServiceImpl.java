package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.domain.user.Role;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.repository.UserRepository;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.web.dto.user.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserService::getById",
            key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found.")
                );
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserService::getByUsername",
            key = "#username"
    )
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User not found with username %s", username)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsersByLocationId(Long id) {
        return userRepository.findAllByLocationId(id);
    }

    @Override
    @Transactional
    public User registerUser(UserDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ResourceNotFoundException("User already exists!");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return cacheUser(user);
    }

    @Caching(cacheable = {
            @Cacheable(
                    value = "UserService::getById",
                    key = "#user.id"),
            @Cacheable(
                    value = "UserService::getByUsername",
                    key = "#user.username")
    })
    private User cacheUser(User user) {
        return user;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(
                    value = "UserService::getById",
                    key = "#id"),
            @CacheEvict(
                    value = "UserService::getByUsername",
                    key = "#user.username")
    })
    public void userEdit(Long id, User user) {
        User userInBase = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found.")
                );
        user.setPassword(userInBase.getPassword());
        Set<Role> roles = user.getRoles();
        if (roles.isEmpty()) {
            roles = Set.of(Role.ROLE_USER);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(
                    value = "UserService::getById",
                    key = "#user.id"),
            @CacheEvict(
                    value = "UserService::getByUsername",
                    key = "#user.username")
    })
    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }
}
