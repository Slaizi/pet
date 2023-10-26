package ru.Bogachev.pet.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Bogachev.pet.domain.entity.LocationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findByUserId (Long id);
    Optional<LocationEntity> findByNameAndUserId (String nameLocation, Long id);
}
