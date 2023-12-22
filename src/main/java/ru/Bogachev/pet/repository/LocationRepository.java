package ru.Bogachev.pet.repository;

import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {
    Optional<Location> findById(Long id);
    Optional<Location> findByName(String locationName);
    List<Location> findAllLocationsByUserId(Long id);
    Optional<Location> findByUserIdAndLocationName(Long userId, String nameLocation);
    void save(User user, Location location);
    void saveUsersLocations(Long userId, Long locationId);
    void delete (Long id);
    void deleteUsersLocationsWhereUserIdAndLocationId(Long userId, Long locationId);

}
