package ru.Bogachev.pet.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.Bogachev.pet.domain.entity.UserEntity;
import ru.Bogachev.pet.domain.repository.UserRepository;
import ru.Bogachev.pet.service.impl.UserServiceImpl;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterUserSuccessful () {
        UserEntity user = mock(UserEntity.class);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        boolean addUser = userService.registerUser(user);

        assertTrue(addUser);
        verify(userRepository, times(1)).save(any());
    }
    @Test
    public void testRegisterFailed() {
        UserEntity user = mock(UserEntity.class);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        boolean addUser = userService.registerUser(user);
        assertFalse(addUser);
        verify(userRepository, times(0)).save(any());
    }
}
