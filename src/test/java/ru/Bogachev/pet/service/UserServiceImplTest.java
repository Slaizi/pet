package ru.Bogachev.pet.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.Bogachev.pet.config.TestConfig;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.domain.user.Role;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.repository.UserRepository;
import ru.Bogachev.pet.service.impl.UserServiceImpl;
import ru.Bogachev.pet.web.dto.user.UserDto;

import java.lang.reflect.Method;
import java.util.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        User testUser = userService.getById(id);
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByIdWithNotExistingId() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(id));
        Mockito.verify(userRepository)
                .findById(id);
    }

    @Test
    void getByUsername() {
        String username = "username";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
        User testUser = userService.getByUsername(username);
        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByUsernameWithNotExistingUsername() {
        String username = "username";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getByUsername(username));
        Mockito.verify(userRepository)
                .findByUsername(username);
    }

    @Test
    void getAllUsers() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new User());
        }
        Mockito.when(userRepository.findAll())
                .thenReturn(list);
        List<User> testList = userService.getAllUsers();
        Mockito.verify(userRepository).findAll();
        Assertions.assertEquals(list, testList);
        Assertions.assertEquals(
                list.size(),
                testList.size()
        );
    }

    @Test
    void getAllUsersByLocationId() {
        Long locationId = 1L;
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new User());
        }
        Mockito.when(userRepository.findAllByLocationId(locationId))
                .thenReturn(list);
        List<User> testList = userService.getAllUsersByLocationId(locationId);
        Mockito.verify(userRepository).findAllByLocationId(locationId);
        Assertions.assertEquals(list, testList);
        Assertions.assertEquals(
                list.size(),
                testList.size()
        );
    }

    @Test
    void registerUser() {
        String password = "password";
        String username = "username";
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setPassword(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        User user = userService.registerUser(dto);
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(
                Set.of(Role.ROLE_USER),
                user.getRoles()
        );
    }

    @Test
    void registerUserWithExistingUser() {
        String username = "username";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
        UserDto dto = new UserDto();
        dto.setUsername(username);
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.registerUser(dto)
        );
    }

    @Test
    @SneakyThrows
    void cacheUser() {
        User user = new User();
        user.setId(1L);
        Method method = UserServiceImpl.class
                .getDeclaredMethod("cacheUser", User.class);
        method.setAccessible(true);
        User cachedUser = (User) method
                .invoke(userService, user);
        Assertions.assertEquals(
                user,
                cachedUser
        );
    }

    @Test
    void userEdit() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setRoles(Collections.emptySet());
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        userService.userEdit(id, user);
        Mockito.verify(userRepository)
                .findById(id);
        Mockito.verify(userRepository)
                .save(user);
        Assertions.assertEquals(
                Set.of(Role.ROLE_USER),
                user.getRoles()
        );
    }

    @Test
    void userEditWithNotExistingUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.userEdit(id, user));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void deleteUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        userService.deleteUser(user);
        Mockito.verify(userRepository)
                .deleteById(id);
    }
}
