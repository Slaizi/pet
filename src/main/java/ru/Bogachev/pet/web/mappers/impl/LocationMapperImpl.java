package ru.Bogachev.pet.web.mappers.impl;

import org.springframework.stereotype.Component;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.web.dto.location.LocationDto;
import ru.Bogachev.pet.web.mappers.LocationMapper;

@Component
public class LocationMapperImpl implements LocationMapper {
    @Override
    public LocationDto toDto(Location location) {
        if(location == null) return null;

        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        return dto;
    }

    @Override
    public Location toEntity(LocationDto dto) {
        if(dto == null) return null;

        Location location = new Location();
        location.setId(dto.getId());
        location.setName(dto.getName());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        return location;
    }
}
