package hiddencore.ddasum.backend.repository;

import hiddencore.ddasum.backend.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // findById(Long id)
}