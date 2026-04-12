package hiddencore.ddasum.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiddencore.ddasum.backend.domain.Facility;
import hiddencore.ddasum.backend.domain.Location;
import hiddencore.ddasum.backend.domain.Patient;
import hiddencore.ddasum.backend.repository.FacilityRepository;
import hiddencore.ddasum.backend.repository.LocationRepository;
import hiddencore.ddasum.backend.repository.PatientRepository;
import hiddencore.ddasum.backend.web.dto.PatientDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 주입, DI
@Transactional(readOnly = true)
public class PatientService {

        private final PatientRepository patientRepository;
        private final FacilityRepository facilityRepository;
        private final LocationRepository locationRepository;

        public List<PatientDto.ListResponse> getPatients() { // 읽기전용 목록 가져오기
                return patientRepository.findAll().stream()
                                .map(PatientDto.ListResponse::from)
                                .toList();
        }

        public PatientDto.DetailResponse getPatient(Long patientId) { // 읽기전용 상세 가져오기
                Patient patient = patientRepository.findById(patientId)
                                .orElseThrow(() -> new IllegalArgumentException("환자가 없습니다. id=" + patientId));

                return PatientDto.DetailResponse.from(patient);
        }

        @Transactional
        public PatientDto.DetailResponse createPatient(PatientDto.CreateRequest request) {
                Facility facility = facilityRepository.findById(1L)
                                .orElseThrow(() -> new IllegalArgumentException("시설이 없습니다. id=1"));

                Location location = null; // 일단 널이라고 가정 -> 병실,병상을 일단 배정안했다는 가정
        
                // 병실 정보가 모두 들어왔는지 확인
                boolean hasLocation = request.getBuilding() != null && !request.getBuilding().isBlank() &&
                                request.getRoom() != null && !request.getRoom().isBlank() &&
                                request.getBed() != null && !request.getBed().isBlank();

                if (hasLocation) { //병실 정보가 다 들어왔을 때만 Location 테이블 조회함 
                        location = locationRepository
                                        .findByBuildingAndRoomAndBed(
                                                        request.getBuilding(),
                                                        request.getRoom(),
                                                        request.getBed())
                                        .orElseThrow(() -> new IllegalArgumentException("해당 병실/병상이 없습니다.")); 
                                        //지금은 입력값으로 받기 때문에 데이터 더미랑 데이터 다르면 예외처리 하는데 나중에 원준오빠꺼랑 합치고
                                        //다른 방식으로 입력하도록 수정할거임
                }

                Patient patient = Patient.builder()
                                .facilityId(facility)
                                .locationId(location)
                                .name(request.getName())
                                .gender(request.getGender())
                                .birthDate(request.getBirthDate())
                                .address(request.getAddress())
                                .admissionDate(request.getAdmissionDate())
                                .type(request.getBloodType())
                                .memo(request.getMemo())
                                .patientStatus(Patient.PatientStatus.STABLE)
                                .build();

                Patient savedPatient = patientRepository.save(patient);
                return PatientDto.DetailResponse.from(savedPatient);
        }

        @Transactional
        public PatientDto.DetailResponse updatePatient(Long patientId, PatientDto.UpdateRequest request) {
                Patient patient = patientRepository.findById(patientId) // 환자 찾기
                                .orElseThrow(() -> new IllegalArgumentException("환자가 없습니다. id=" + patientId));

                Facility facility = facilityRepository.findById(request.getFacilityId()) // 시설찾기
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "시설이 없습니다. id=" + request.getFacilityId()));

                patient.setFacilityId(facility);
                patient.setName(request.getName());
                patient.setGender(request.getGender());
                patient.setBirthDate(request.getBirthDate());
                patient.setAdmissionDate(request.getAdmissionDate());
                patient.setDischargeDate(request.getDischargeDate());
                patient.setType(request.getBloodType());
                patient.setHeight(request.getHeight());
                patient.setWeight(request.getWeight());
                patient.setAdmissionStatus(request.getAdmissionStatus());
                patient.setDietType(request.getDietType());
                patient.setMemo(request.getMemo());
                patient.setPatientStatus(request.getPatientStatus());

                return PatientDto.DetailResponse.from(patient);
        }
}