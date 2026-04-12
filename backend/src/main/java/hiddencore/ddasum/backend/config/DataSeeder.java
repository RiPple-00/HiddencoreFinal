package hiddencore.ddasum.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hiddencore.ddasum.backend.domain.Facility;
import hiddencore.ddasum.backend.repository.FacilityRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedFacility(FacilityRepository facilityRepository) {
        return args -> {
            if (facilityRepository.count() > 0) {
                return;
            }

            Facility facility = Facility.builder()
                    .name("기본 시설")
                    .address("서울")
                    .phone("000-0000-0000")
                    .build();

            facilityRepository.save(facility);
        };
    }
}
