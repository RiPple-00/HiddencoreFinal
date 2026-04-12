package hiddencore.ddasum.backend.web;

import hiddencore.ddasum.backend.service.BedRoomService;
import hiddencore.ddasum.backend.web.dto.AssignPatientToBedRequest;
import hiddencore.ddasum.backend.web.dto.BedResponseDto;
import hiddencore.ddasum.backend.web.dto.PatientAssignSearchResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class BedRoomController {

    private final BedRoomService bedRoomService;

    /** 병실별 병상 배치도 조회 */
    @GetMapping("/{room}/beds")
    public ResponseEntity<List<BedResponseDto>> getBedsByRoom(@PathVariable String room) {
        List<BedResponseDto> beds = bedRoomService.getBedsByRoom(room);
        return ResponseEntity.ok(beds);
    }

    /** 병상 배정을 위한 환자 검색 */
    @GetMapping("/patients/search")
    public ResponseEntity<List<PatientAssignSearchResponseDto>> searchPatientsForAssign(
            @RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(bedRoomService.getSearchPatientsForAssign(keyword));
    }

    /** 환자를 지정 침상(location)에 배정 */
    @PutMapping("/beds/{locationId}/assign")
    public ResponseEntity<Void> assignPatientToBed(
            @PathVariable Long locationId,
            @RequestBody AssignPatientToBedRequest body) {
        if (body == null || body.getPatientId() == null) {
            throw new IllegalArgumentException("patientId가 필요합니다.");
        }
        bedRoomService.assignPatientToBed(locationId, body.getPatientId());
        return ResponseEntity.noContent().build();
    }

    // /*환자 상세 정보 불러오기*/
    // @GetMapping("/patients/${patientId}")
    // public ResponseEntity<>{
        
    // }
    

    /*배정 환자 해제*/
    
}
