package ru.Bogachev.pet.web.mappers;

import org.mapstruct.Mapper;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.web.dto.location.LocationDto;
@Mapper(componentModel = "spring")
public interface LocationMapper extends Mappable<Location, LocationDto> {
}
