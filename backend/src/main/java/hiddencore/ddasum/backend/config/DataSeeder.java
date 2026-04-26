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
