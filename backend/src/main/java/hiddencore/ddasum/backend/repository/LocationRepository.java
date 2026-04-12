package hiddencore.ddasum.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hiddencore.ddasum.backend.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByBuildingAndRoomAndBed(String building, String room, String bed);
}