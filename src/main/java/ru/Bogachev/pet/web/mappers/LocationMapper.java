package ru.Bogachev.pet.web.mappers;

import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.web.dto.location.LocationDto;

public interface LocationMapper {
    LocationDto toDto (Location location);
    Location toEntity (LocationDto dto);
}
