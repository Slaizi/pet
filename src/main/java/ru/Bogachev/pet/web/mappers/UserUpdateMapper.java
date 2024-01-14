package ru.Bogachev.pet.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.web.dto.user.UpdateUserDto;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserUpdateMapper extends Mappable<User, UpdateUserDto> {
    @Override
    @Mapping(target = "roles", source = "roles")
    UpdateUserDto toDto(User entity);

    @Override
    @Mapping(target = "roles", source = "roles")
    List<UpdateUserDto> toDto(List<User> entity);

    @Override
    @Mapping(target = "roles", source = "roles")
    User toEntity(UpdateUserDto dto);

    @Override
    @Mapping(target = "roles", source = "roles")
    List<User> toEntity(List<UpdateUserDto> dto);
}
