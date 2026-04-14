package hiddencore.ddasum.backend.web.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import hiddencore.ddasum.backend.domain.MealPlan.Mealtype;

public class MealPlanDto {

    /**
     * 📌 1. 엑셀 업로드 요청
     */
    @Data
    public static class BulkUpsertRequest {
        private Long facilityId;
        private Long adminId;
        private List<RowRequest> rows;
    }

    /**
     * 📌 2. 한 줄 데이터 (프론트 → 백)
     */
    @Data
    public static class RowRequest {
        private LocalDate mealDate;
        private String mealType;   // "BREAKFAST"
        private String dietType;   // "GENERAL"
        private String menu;
    }

    /**
     * 📌 3. 업로드 결과 응답
     */
    @Data
    @Builder
    public static class BulkUpsertResponse {
        private int requestedCount;
        private int savedCount;
    }

    /**
     * 📌 4. 조회용 (캘린더)
     */
    @Data
    @Builder
    public static class MealPlanResponse {
        private Long mealPlanId;
        private Long facilityId;
        private Long adminId;

        private LocalDate mealDate;
        private String mealType;
        private String dietType;
        private String menu;

        private LocalDateTime createdAt;
    }

    /**
     * 📌 5. 수정용
     */
    @Data
    public static class UpdateRequest {
        private String menu;
    }
}