package hiddencore.ddasum.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hiddencore.ddasum.backend.domain.Location;

public interface LocationRepository extends JpaRepository<Location,Long> {
    
    List<Location> findByRoomOrderByBedAsc(String room);
}
