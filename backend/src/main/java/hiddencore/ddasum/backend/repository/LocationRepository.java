package hiddencore.ddasum.backend.repository;

import hiddencore.ddasum.backend.domain.Location;
import hiddencore.ddasum.backend.web.dto.LocationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

@Query("""
SELECT new hiddencore.ddasum.backend.web.dto.LocationDto$RoomPatientCountDto(
    l.building,
    l.floor,
    l.room,
    l.roomType,
    l.roomGenderType,
    COUNT(l),
    SUM(CASE WHEN p.patientId IS NOT NULL THEN 1 ELSE 0 END),
    MAX(l.roomCapacity)
)
FROM Location l
LEFT JOIN Patient p ON p.locationId = l AND p.dischargeDate IS NULL
WHERE l.building = :building
  AND l.floor = :floor
GROUP BY l.building, l.floor, l.room, l.roomType, l.roomGenderType
ORDER BY l.room
""")
    List<LocationDto.RoomPatientCountDto> findRoomSummaryByBuildingAndFloor(
        @Param("building") String building,
        @Param("floor") Integer floor
    );

    @Query("""
    SELECT COUNT(p)
    FROM Patient p
    WHERE p.locationId.building = :building
      AND p.locationId.floor = :floor
      AND p.locationId.room = :room
      AND p.dischargeDate IS NULL
    """)
    long countActivePatientsInRoom(
        @Param("building") String building,
        @Param("floor") Integer floor,
        @Param("room") String room
    );
}