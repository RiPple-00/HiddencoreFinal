package hiddencore.ddasum.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hiddencore.ddasum.backend.domain.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // findById(Long id)
}
