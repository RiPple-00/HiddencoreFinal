package hiddencore.ddasum.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hiddencore.ddasum.backend.domain.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {}
