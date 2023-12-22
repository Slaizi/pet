package ru.Bogachev.pet.web.mappers.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.Bogachev.pet.domain.user.Role;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.user.UserDto;
import ru.Bogachev.pet.web.mappers.UserMapper;
import ru.Bogachev.pet.web.security.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toDto(User user) {
        if(user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRoles(user.getRoles());

        return dto;
    }

    @Override
    public UserDto toDtoWhereUserDetails(UserDetails userDetails) {
        if(userDetails == null) return null;

        UserDto dto = new UserDto();
        dto.setId(userDetails.getId());
        dto.setUsername(userDetails.getUsername());
        dto.setPassword(userDetails.getPassword());
        dto.setRoles(mapToSetRoles(userDetails.getAuthorities()));

        return dto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if(dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRoles(dto.getRoles());

        return user;
    }

    @Override
    public User toEntityWhereUserService(UserDetails userDetails) {
        if(userDetails == null) return null;

        User user = new User();
        user.setId(userDetails.getId());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setRoles(mapToSetRoles(userDetails.getAuthorities()));

        return user;
    }
    private static Set<Role> mapToSetRoles (Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(e -> Role.valueOf(e.getAuthority()))
                .collect(Collectors.toSet());
    }
}
