package ru.Bogachev.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.Bogachev.pet.domain.location.Location;

import java.util.List;
import java.util.Optional;
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByName(String locationName);
    @Query(value = """
            SELECT * FROM locations l
            LEFT JOIN users_locations ul on ul.location_id = l.id
            WHERE ul.user_id = :userId
            """, nativeQuery = true)
    List<Location> findAllByUserId(@Param("userId") Long userId);

    @Query(value = """
            SELECT EXISTS (
                        SELECT 1
                        FROM users_locations
                        WHERE user_id = :userId
                        AND location_id = :locationId
                      )
            """, nativeQuery = true)
    boolean locationHasUser(@Param("userId") Long userId,
                            @Param("locationId") Long locationId);

    @Modifying
    @Query(value = """
            INSERT INTO users_locations (user_id, location_id)
            VALUES (:userId, :locationId)
            """, nativeQuery = true)
    void assignLocation(@Param("userId") Long userId,
                        @Param("locationId") Long locationId);
    @Modifying
    @Query(value = """
            DELETE FROM users_locations ul
            WHERE ul.user_id = :userId
            AND ul.location_id = :locationId
            """, nativeQuery = true)
    void removeLocation(@Param("userId") Long userId,
                        @Param("locationId") Long locationId);

}
