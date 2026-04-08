package hiddencore.ddasum.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hiddencore.ddasum.backend.domain.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}