package hiddencore.ddasum.backend.service;

import hiddencore.ddasum.backend.repository.LocationRepository;
import hiddencore.ddasum.backend.web.dto.LocationDto;
import hiddencore.ddasum.backend.web.dto.LocationDto.RoomPatientCountDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<RoomPatientCountDto> getRoomSummary(String building, Integer floor) {
        return locationRepository.findRoomSummaryByBuildingAndFloor(building, floor);
    }

    @Transactional(readOnly = true)
    public LocationDto.RoomPatientCountDto getRoomPatientCount(String building, Integer floor, String room) {
        long count = locationRepository.countActivePatientsInRoom(building, floor, room);
        return LocationDto.RoomPatientCountDto.builder()
                .building(building)
                .floor(floor)
                .room(room)
                .patientCount(count)
                .build();
    }
}
