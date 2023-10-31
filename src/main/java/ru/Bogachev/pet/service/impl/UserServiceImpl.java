package ru.Bogachev.pet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.entity.enums.Role;
import ru.Bogachev.pet.domain.repository.UserRepository;
import ru.Bogachev.pet.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(UserEntity user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void userEdit(UserEntity user, String username, List<String> roles) {
        user.setUsername(username);
        if(!roles.isEmpty()) {
            Set<Role> setRoles = roles.stream()
                                      .map(Role::valueOf)
                                      .collect(Collectors.toSet());
            user.setRoles(setRoles);
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }

}
