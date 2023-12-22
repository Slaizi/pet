package ru.Bogachev.pet.repository;

import ru.Bogachev.pet.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    List<User> findAll();
    List<User> findAllUsersByLocationId (Long locationId);
    Optional<User> findByUsername(String username);
    void update(User user);
    void save(User user);
    void delete(Long id);
}
