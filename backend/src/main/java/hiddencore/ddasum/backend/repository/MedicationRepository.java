package hiddencore.ddasum.backend.repository;

import hiddencore.ddasum.backend.domain.Medication;
import hiddencore.ddasum.backend.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long>{
    List<Medication> findByPatientIdOrderByPrescriptionDateDesc(Patient patientId);
}