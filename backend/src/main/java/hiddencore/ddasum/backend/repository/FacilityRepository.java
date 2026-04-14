package hiddencore.ddasum.backend.repository;

<<<<<<< HEAD
import hiddencore.ddasum.backend.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // findById(Long id)
}
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hiddencore.ddasum.backend.domain.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {}
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
