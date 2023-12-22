package ru.Bogachev.pet.web.mappers;

import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.user.UserDto;
import ru.Bogachev.pet.web.security.UserDetails;


public interface UserMapper {
    UserDto toDto(User user);
    UserDto toDtoWhereUserDetails(UserDetails userDetails);
    User toEntity(UserDto dto);
    User toEntityWhereUserService(UserDetails userDetails);
}
