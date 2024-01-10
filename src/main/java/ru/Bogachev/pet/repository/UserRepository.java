package ru.Bogachev.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.Bogachev.pet.domain.user.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query(value = """
        SELECT * FROM users u
        JOIN users_locations ul on ul.user_id = u.id
        WHERE ul.location_id = :locationId
        """, nativeQuery = true)
    List<User> findAllByLocationId(@Param("locationId") Long locationId);
}
