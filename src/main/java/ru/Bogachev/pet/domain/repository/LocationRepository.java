package ru.Bogachev.pet.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Bogachev.pet.domain.entity.LocationEntity;
import ru.Bogachev.pet.domain.entity.UserEntity;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findByUserId (Long id);
}
