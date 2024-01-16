package ru.Bogachev.pet.web.mappers;

import org.mapstruct.Mapper;
import ru.Bogachev.pet.web.dto.user.UserDetailsDto;
import ru.Bogachev.pet.web.security.UserDetails;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper extends Mappable<
        UserDetails,
        UserDetailsDto> {
}
