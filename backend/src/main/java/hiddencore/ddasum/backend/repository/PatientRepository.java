package hiddencore.ddasum.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hiddencore.ddasum.backend.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByLocationId_LocationId(Long locationId);

    List<Patient> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT p FROM Patient p LEFT JOIN FETCH p.locationId WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Patient> searchByNameContaining(@Param("keyword") String keyword);
}
