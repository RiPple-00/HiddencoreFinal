package hiddencore.ddasum.backend.web;

import lombok.RequiredArgsConstructor;

import hiddencore.ddasum.backend.service.PatientService;
import hiddencore.ddasum.backend.web.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping // 전체 목록 조회
    public ResponseEntity<List<PatientDto.ListResponse>> getPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    @GetMapping("/{patientId}") // 환자 상세 조회
    public ResponseEntity<PatientDto.DetailResponse> getPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    @PostMapping // 환자 생성
    public ResponseEntity<PatientDto.DetailResponse> createPatient(
            @RequestBody PatientDto.CreateRequest request) {
        return new ResponseEntity<>(patientService.createPatient(request), HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}") // 환자 정보 수정
    public ResponseEntity<PatientDto.DetailResponse> updatePatient(
            @PathVariable Long patientId,
            @RequestBody PatientDto.UpdateRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, request));
    }
}
