package hiddencore.ddasum.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PATIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location locationId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "address", length = 255) // 주소 칼럼 추가
    private String address;

    @Column(name = "admission_date", nullable = false) // 입원 날짜
    private LocalDate admissionDate;

    @Column(name = "discharge_date") // 퇴원날짜
    private LocalDate dischargeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", nullable = false)
    private BloodType type;

    @Column(name = "height", precision = 5, scale = 2) // precision 총 자리수, scale = 소수점 표현 자리수
    private BigDecimal height; // BigDecimal 더 정확한 소수점

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "admission_status", length = 100)
    private String admissionStatus;

    @Column(name = "diet_type", length = 50)
    private String dietType;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "patient_status", nullable = false, length = 30)
    private PatientStatus patientStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BloodType {
        A_POSITIVE, // A+
        A_NEGATIVE, // A-
        B_POSITIVE, // B+
        B_NEGATIVE, // B-
        AB_POSITIVE, // AB+
        AB_NEGATIVE, // AB-
        O_POSITIVE, // O+
        O_NEGATIVE // O-
    }

    public enum Gender { // 성별 ENUM 추가
        MALE,
        FEMALE,
        OTHER
    }

    public enum PatientStatus {
        STABLE, // 안정
        DISCHARGE_SOON, // 퇴원예정
        FOCUSED_CARE, // 집중관찰
        DANGER, // 위험
        DISCHARGED // 퇴원
    }
}
