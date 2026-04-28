package hiddencore.ddasum.backend.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import hiddencore.ddasum.backend.domain.Facility;
import hiddencore.ddasum.backend.domain.Users;
import hiddencore.ddasum.backend.domain.Users.UsersRole;
import hiddencore.ddasum.backend.domain.Users.UsersStatus;
import hiddencore.ddasum.backend.repository.FacilityRepository;
import hiddencore.ddasum.backend.repository.MemberRepository;
import hiddencore.ddasum.backend.security.StaffLoginIdCodec;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAuthDemo(
            FacilityRepository facilityRepository, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            for (Facility f : facilityRepository.findAll()) {
                if (f.getFacilityCode() == null || f.getFacilityCode().isBlank()) {
                    String code = String.format("%08d", Math.min(99_999_999, f.getFacilityId().intValue()));
                    f.setFacilityCode(code);
                    facilityRepository.save(f);
                }
            }

            Facility facility =
                    facilityRepository
                            .findByFacilityCode("12345678")
                            .orElseGet(
                                    () ->
                                            facilityRepository.save(
                                                    Facility.builder()
                                                            .name("데모 요양병원")
                                                            .address("서울특별시")
                                                            .phone("02-0000-0000")
                                                            .facilityCode("12345678")
                                                            .build()));

            if (facility.getFacilityCode() == null || facility.getFacilityCode().isBlank()) {
                facility.setFacilityCode("12345678");
                facility = facilityRepository.save(facility);
            }

            String demoEmployeeId = "1120010101";
            String loginId = StaffLoginIdCodec.encode(facility.getFacilityCode(), demoEmployeeId);
            Users office =
                    memberRepository
                            .findByLoginId(loginId)
                            .orElse(
                                    Users.builder()
                                            .loginId(loginId)
                                            .employeeLoginId(demoEmployeeId)
                                            .build());
            office.setFacilityId(facility);
            office.setEmployeeLoginId(demoEmployeeId);
            office.setHireDate(LocalDate.of(2020, 1, 1));
            office.setPassword(passwordEncoder.encode("office123!"));
            office.setName("데모 원무");
            office.setPhone("01010000001");
            office.setEmail("office-demo@ddasum.local");
            office.setRole(UsersRole.OFFICE);
            office.setStatus(UsersStatus.ACTIVE);
            office.setMustChangePassword(false);
            office.setEmailAgreed(true);
            office.setEmailAgreedAt(LocalDateTime.now());
            memberRepository.save(office);

            String demoDoctorEmployeeId = "2120010101";
            String doctorLoginId = StaffLoginIdCodec.encode(facility.getFacilityCode(), demoDoctorEmployeeId);
            Users doctor =
                    memberRepository
                            .findByLoginId(doctorLoginId)
                            .orElse(
                                    Users.builder()
                                            .loginId(doctorLoginId)
                                            .employeeLoginId(demoDoctorEmployeeId)
                                            .build());
            doctor.setFacilityId(facility);
            doctor.setEmployeeLoginId(demoDoctorEmployeeId);
            doctor.setHireDate(LocalDate.of(2020, 1, 1));
            doctor.setPassword(passwordEncoder.encode("office123!"));
            doctor.setName("데모 의사");
            doctor.setPhone("01020000001");
            doctor.setEmail("doctor-demo@ddasum.local");
            doctor.setRole(UsersRole.DOCTOR);
            doctor.setStatus(UsersStatus.ACTIVE);
            doctor.setMustChangePassword(false);
            doctor.setEmailAgreed(true);
            doctor.setEmailAgreedAt(LocalDateTime.now());
            memberRepository.save(doctor);

            String demoCaregiverEmployeeId = "3120010101";
            String caregiverLoginId = StaffLoginIdCodec.encode(facility.getFacilityCode(), demoCaregiverEmployeeId);
            Users caregiver =
                    memberRepository
                            .findByLoginId(caregiverLoginId)
                            .orElse(
                                    Users.builder()
                                            .loginId(caregiverLoginId)
                                            .employeeLoginId(demoCaregiverEmployeeId)
                                            .build());
            caregiver.setFacilityId(facility);
            caregiver.setEmployeeLoginId(demoCaregiverEmployeeId);
            caregiver.setHireDate(LocalDate.of(2020, 1, 1));
            caregiver.setPassword(passwordEncoder.encode("office123!"));
            caregiver.setName("데모 요양사");
            caregiver.setPhone("01030000001");
            caregiver.setEmail("caregiver-demo@ddasum.local");
            caregiver.setRole(UsersRole.CAREGIVER);
            caregiver.setStatus(UsersStatus.ACTIVE);
            caregiver.setMustChangePassword(false);
            caregiver.setEmailAgreed(true);
            caregiver.setEmailAgreedAt(LocalDateTime.now());
            memberRepository.save(caregiver);

            Users guardian =
                    memberRepository
                            .findByLoginId("guardian001")
                            .orElse(Users.builder().loginId("guardian001").build());
            guardian.setFacilityId(null);
            guardian.setEmployeeLoginId(null);
            guardian.setHireDate(null);
            guardian.setPassword(passwordEncoder.encode("1234"));
            guardian.setName("데모 보호자");
            guardian.setPhone("01099998888");
            guardian.setEmail("guardian-demo@ddasum.local");
            guardian.setRole(UsersRole.GUARDIAN);
            guardian.setStatus(UsersStatus.ACTIVE);
            guardian.setMustChangePassword(false);
            guardian.setEmailAgreed(true);
            guardian.setEmailAgreedAt(LocalDateTime.now());
            memberRepository.save(guardian);
        };
    }
}
