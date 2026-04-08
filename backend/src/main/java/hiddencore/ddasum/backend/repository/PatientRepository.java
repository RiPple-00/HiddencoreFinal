package hiddencore.ddasum.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hiddencore.ddasum.backend.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}