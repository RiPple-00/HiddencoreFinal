package hiddencore.ddasum.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiddencore.ddasum.backend.domain.Facility;
import hiddencore.ddasum.backend.domain.Patient;
import hiddencore.ddasum.backend.repository.FacilityRepository;
import hiddencore.ddasum.backend.repository.PatientRepository;
import hiddencore.ddasum.backend.web.dto.PatientDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 주입, DI
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;
    private final FacilityRepository facilityRepository;

    @Transactional // 환자 생성 메서드
    public PatientDto.DetailResponse createPatient(PatientDto.CreateRequest request) {
        Facility facility = facilityRepository.findById(request.getFacilityId()) //시설ID가 숫자로 들어와도 엔티티에 저장할때는 Facilty(1번 시설 객체!!)
                .orElseThrow(() -> new IllegalArgumentException("시설이 없습니다. id=" + request.getFacilityId()));

        Patient patient = Patient.builder()
                .facilityId(facility)
                .name(request.getName())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .admissionDate(request.getAdmissionDate())
                .dischargeDate(request.getDischargeDate())
                .type(request.getBloodType()) //dto는 booldType이라 type으로 저장
                .height(request.getHeight())
                .weight(request.getWeight())
                .admissionStatus(request.getAdmissionStatus())
                .dietType(request.getDietType())
                .memo(request.getMemo())
                .patientStatus(request.getPatientStatus())
                .build();

        Patient savedPatient = patientRepository.save(patient); //저장
        return PatientDto.DetailResponse.from(savedPatient);
    }

    public List<PatientDto.ListResponse> getPatients() { //환자 리스트 조회
        return patientRepository.findAll().stream() 
                .map(PatientDto.ListResponse::from)
                .toList();
    }

    public PatientDto.DetailResponse getPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId) //환자 찾기
                .orElseThrow(() -> new IllegalArgumentException("환자가 없습니다. id=" + patientId));

        return PatientDto.DetailResponse.from(patient);
    }

    @Transactional
    public PatientDto.DetailResponse updatePatient(Long patientId, PatientDto.UpdateRequest request) {
        Patient patient = patientRepository.findById(patientId) //환자 찾기
                .orElseThrow(() -> new IllegalArgumentException("환자가 없습니다. id=" + patientId));

        Facility facility = facilityRepository.findById(request.getFacilityId()) //시설찾기
                .orElseThrow(() -> new IllegalArgumentException("시설이 없습니다. id=" + request.getFacilityId()));

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