package ru.Bogachev.pet.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.service.UserService;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements
        org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(
            final String username
    ) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        return UserDetailsFactory.create(user);
    }
}
