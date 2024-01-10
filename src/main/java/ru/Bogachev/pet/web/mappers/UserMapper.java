package ru.Bogachev.pet.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.user.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
    @Override
    @Mapping(target = "roles", source = "roles")
    UserDto toDto(User entity);

    @Override
    @Mapping(target = "roles", source = "roles")
    List<UserDto> toDto(List<User> entity);

    @Override
    @Mapping(target = "roles", source = "roles")
    User toEntity(UserDto dto);

    @Override
    @Mapping(target = "roles", source = "roles")
    List<User> toEntity(List<UserDto> dto);
}
