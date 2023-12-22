package ru.Bogachev.pet.repository.mappers;

import lombok.SneakyThrows;
import ru.Bogachev.pet.domain.location.Location;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LocationRowMapper {
    @SneakyThrows
    public static Location mapRow (ResultSet resultSet) {
        if (resultSet.next()) {
            Location location = new Location();
            location.setId(resultSet.getLong("location_id"));
            location.setName(resultSet.getString("location_name"));
            location.setLatitude(resultSet.getDouble("location_latitude"));
            location.setLongitude(resultSet.getDouble("location_longitude"));
            return location;
        }
        return null;
    }
    @SneakyThrows
    public static List<Location> mapRows (ResultSet resultSet) {
        List<Location> locations = new ArrayList<>();
        while(resultSet.next()) {
            Location location = new Location();
            location.setId(resultSet.getLong("location_id"));
            if(!resultSet.wasNull()) {
                location.setName(resultSet.getString("location_name"));
                location.setLatitude(resultSet.getDouble("location_latitude"));
                location.setLongitude(resultSet.getDouble("location_longitude"));
            }
            locations.add(location);
        }
        return locations;
    }
}
